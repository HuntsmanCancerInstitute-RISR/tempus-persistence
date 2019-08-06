package hci.ri.tempus.model;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import hci.ri.tempus.util.PeekableScanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TempusParser {

    private String tempusCredFile;
    private String downloadPath;
    private String tempusJsonFileList;
    private String deidentFile;
    private String outFileName;
    private static final String SCHEMA_VER_ACCEPTED = "1.3";
    private static final Integer SAMPLE_ID_START= 7;
    private static final Integer SAMPLE_ID_END = 13;
    private Map<String,Specimen> specimenMap;
    private static final Integer nucLen = 4; // ex -RNA
    private ObjectMapper objectMapper;


    public TempusParser(String[] args){

        for(int i = 0; i < args.length; i++){
            args[i] = args[i].toLowerCase();
            if(args[i].equals("-cred")){
                tempusCredFile = args[++i];
            }else if(args[i].equals("-download")){
                downloadPath = args[++i];
            }else if(args[i].equals("-json")){
                tempusJsonFileList = args[++i];
            }else if(args[i].equals("-deidentjson")){
                deidentFile = args[++i];
            }else if(args[i].equals("-out")){
                outFileName = args[++i];
            }else if(args[i].equals("-help")){
                help();
                System.exit(0);
            }
        }

        if(downloadPath == null){
            downloadPath = "";
        }

        if(tempusJsonFileList == null || tempusCredFile == null || deidentFile == null){
            help();
            System.exit(1);
        }

        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Report.class, new ReportDeserializer());
        module.addDeserializer(Specimen.class, new SpecimenDeserializer());
        module.addDeserializer(Variant.class, new VariantDeserializer());
        module.addDeserializer(SPActionableCPVariant.class, new CopyNumberVariantDeserializer());
        module.addDeserializer(Object.class, new IhcFindingDeserializer());
        objectMapper.registerModule(module);


        Map<String,String> propMap = new HashMap<String,String>();
        specimenMap = new HashMap<>();
    }

    private void help(){
        System.out.println("-cred : a file credentials need for hibernate to access database");
        System.out.println("-download : download path used for the location of your json/files to read in");
        System.out.println("-json : tempus json file to be parsed and ingested to the database");
        System.out.println("-deidentjson: The tempus deidentified json output file");
        System.out.println("-out: The output of sample info metadata used in tempus pipeline");
    }

    public Map loadCred() {
        Map propMap = new HashMap<String,String>();
        try(InputStream input = new FileInputStream(tempusCredFile)) {
            Properties prop = new Properties();
            prop.load(input);
            prop.forEach((key,value) -> propMap.put(key,value));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return propMap;
    }

    public void parseTempusFileList(List<TempusFile> tempusFileList, Map<String,List<TempusSample>> tempusOtherFileList, EntityManager manager){
        StringBuilder jsonPathPlusFile = new StringBuilder();
        List<String> linkableJsonList = new ArrayList<>();
        PeekableScanner sc = null;

        try{
            sc = new PeekableScanner(new File( tempusJsonFileList));

            boolean foundRNA = false; // RNA seq
            boolean foundTumor = false;
            boolean foundNormal = false;
            Map<String,String> sampleNameMap = new HashMap<String,String>();

            while(sc.hasNext()){
                String line = sc.next();
                String[] fileChunks = getFileChunks(line,"/");//File.separator);
                String path = "";
                String fileName = fileChunks[fileChunks.length - 1];
                String[] fileTypeChunks = getFileChunks(fileName, "\\.");
                String fileType = fileTypeChunks[fileTypeChunks.length - 1];

                if(fileChunks.length > 1){
                    path = String.join(File.separator, Arrays.copyOfRange(fileChunks, 0, fileChunks.length - 1) );
                    jsonPathPlusFile.append(path);
                    jsonPathPlusFile.append(File.separator);
                    jsonPathPlusFile.append(fileName);
                }else {
                    jsonPathPlusFile.append(downloadPath);
                    jsonPathPlusFile.append(File.separator);
                    jsonPathPlusFile.append(fileName);
                }


                if(fileType.equals("json")){
                    TempusFile tFile = parseTempusFile(jsonPathPlusFile.toString());
                    findLinkableJsons(manager, fileName, tempusOtherFileList, tFile );
                    tempusFileList.add(tFile);

                }else if(fileType.equals("md5") || fileType.equals("pdf")){
                    //todo not sure if I need to keep track of these
                }else {
                    String[] chunks =  fileName.split("_");
                    String fileTestType = "";

                    String excludeNucFilename = chunks[0].substring(0, chunks[0].length() - nucLen);
                    List<TempusSample> samples  = tempusOtherFileList.get(excludeNucFilename);
                    String sampleName = String.join("_", Arrays.copyOfRange(chunks, 0, chunks.length - 1) );
                    TempusSample rnaSample = new TempusSample();


                    if(samples == null){
                        System.out.println("WARNING: " + line + " does not have a json file associated with it or is not linkable to json");
                        continue;
                    }


                    for(String fileChunk : chunks ){
                        if(fileChunk.equals("N")){
                            fileTestType = "normal";
                            break;
                        }else if(fileChunk.equals("T")){
                            fileTestType = "tumor";
                            break;
                        }else if(fileChunk.equals("RSQ1")){
                            fileTestType = "RSQ1";
                            foundRNA = true; // don't know if tempus provides rna seq sample meta data
                            rnaSample.setSampleName(sampleName);
                            rnaSample.setTestType("tumor");
                            rnaSample.setPersonId(samples.get(0).getPersonId()); // should always be atleast one entry in array for its key
                            rnaSample.setMrn(samples.get(0).getMrn());
                            rnaSample.setGender(samples.get(0).getGender());
                            rnaSample.setFullName(samples.get(0).getFullName());
                            break;
                        }
                    }
                    // if sample metadata exists but not the file then the sampleName never gets set and gets flagged
                    for(TempusSample sample : samples ){
                        String testType = sample.getTestType().toLowerCase();
                        if(testType.equals(fileTestType) && testType.equals("tumor")){
                            foundTumor = true;
                            sample.setSampleName(sampleName);
                            break;
                        }
                        if(testType.equals(fileTestType) && testType.equals("normal")){
                            foundNormal = true;
                            sample.setSampleName(sampleName);
                            break;
                        }
                    }

                    sampleNameMap.put(fileTestType,sampleName);



                    if(isNewPatientFile(sc, chunks)){
                        if(foundRNA){
                            samples.add(rnaSample);
                        }

                        if(!foundNormal){
                            System.out.println("WARNING: didn't find the metadata for normal of sample " + chunks[0] );
                            String sName = sampleNameMap.get("normal");
                            if(sName != null){
                                // this is the case where there is a file but no sample metadata for it
                                // this is how we get it flagged in the next step because its missing idPerson
                                samples.add(new TempusSample(sName));
                            }

                        }
                        if(!foundTumor){
                            System.out.println("WARNING: didn't find the metadata for tumor of sample " + chunks[0]);
                            String sName = sampleNameMap.get("tumor");
                            if(sName != null){
                                // this is the case where there is a file but no sample metadata for it
                                // this is how we get it flagged in the next step because its missing idPerson
                                samples.add(new TempusSample(sName));
                            }
                        }
                        foundRNA = false;
                        foundTumor = false;
                        foundNormal = false;
                        sampleNameMap.clear();
                    }

                    //tempusOtherFileList.put(,jsonPathPlusFile.toString());
                }
                jsonPathPlusFile.setLength(0);


            }


        }catch(Exception e){
            e.printStackTrace();
            if(sc != null){
                sc.close();
            }
            System.exit(1);
        }finally{
            if(sc != null){
                sc.close();
            }

        }
    }

    public boolean isNewPatientFile(PeekableScanner sc,String[] chunks){
        boolean isNewPatientFile = false;
        String nextLine = sc.peek();
        if(nextLine != null){
            String[] pathChunks = getFileChunks(nextLine,"/");// "\\"+File.separator);
            String nextFile = pathChunks[ pathChunks.length - 1];
            String[] nextChunks = nextFile.split("_");
            String[] fileExtChunks = nextChunks[nextChunks.length - 1].split("\\.");
            String nextLastExt = fileExtChunks[fileExtChunks.length - 1];
            // fastq.gz not fastq.gz.md5
            if(!nextLastExt.equals("gz")){
                isNewPatientFile = true;
            }
            if(!chunks[0].equals(nextChunks[0])){
                isNewPatientFile = true;
            }
        }else{
            isNewPatientFile = true;
        }

        return isNewPatientFile;
    }

    private void scrubJsonFile(TempusFile tFile, String fileWithPath ) throws IOException {
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        File f = new File(fileWithPath);
        String name = f.getName();
        String path = f.getParent();
        String[] fileChunks = name.split("\\.");
        if(fileChunks.length == 2 && fileChunks[1].equals("json")){
            String nameNoExt = fileChunks[0];
            tFile.getPatient().setDoB("xxx");
            tFile.getPatient().setFirstName("xxx");
            tFile.getPatient().setLastName("xxx");
            tFile.getPatient().setTempusId("xxx");
            writer.writeValue(new File(path +"\\" + nameNoExt + ".deident.json"),tFile);

        }

    }

/*
    private String getFileTypeChunks(String file, String splitStr){
        String[] fileChunks = file.split(splitStr);
        boolean fastqFound = false;

        if(fileChunks[1].equals("json") || fileChunks[1].equals("pdf")){
            return fileChunks[1];
        }else{
            for(int i = 0; i < fileChunks.length; i++){
                if(i == 0){
                    continue;
                }else if(fileChunks[i].equals("fastq")){
                   fastqFound = true;
                   break;
                }

            }

            if(fastqFound){
                return String.join(".", Arrays.copyOfRange(fileChunks, 1, fileChunks.length ));
            }

        }
        return "";
    } */

    private String[] getFileChunks(String file, String splitStr){
        String[] fileChunks = file.split(splitStr);
        return fileChunks;
    }


    public TempusFile parseTempusFile(String tempusJson) throws Exception{

        byte[] jsonData = Files.readAllBytes(Paths.get(tempusJson));
        TempusFile tf = objectMapper.readValue(jsonData, TempusFile.class);
        if(!tf.getMetadata().getSchemaVersion().equals(SCHEMA_VER_ACCEPTED)){
            throw new Exception("JSON schema version is incompatible with parser ");
        }

        if(tf.getResults().getInheritedRelevantVariants() != null){
            for(InheritedVariant relevantVariant:tf.getResults().getInheritedRelevantVariants()){
                relevantVariant.setVariantCategory("InheritedRelevantVariant");
            }
        }
        if(tf.getResults().getInheritedVariantsOfUnknownSignificance() != null){
            for(InheritedVariant unknownVariant : tf.getResults().getInheritedVariantsOfUnknownSignificance()){
                unknownVariant.setVariantCategory("InheritedVariantsOfUnknownSignificance");
            }
        }


        return tf;
    }
    public  void importTempusFile(EntityManager manager, List<TempusFile> tfList){
        EntityTransaction transaction = null;
        try{
            for(TempusFile tf : tfList){
                if(isUniqueTempusFile(tf, manager)){
                    System.out.println("Now importing " + tf.getOrder().getAccessionId());
                    transaction= manager.getTransaction();
                    transaction.begin();
                    manager.persist(tf);
                    transaction.commit();
                }else {
                    System.out.println("This json has already been imported for " + tf.getOrder().getAccessionId()+ "... Skipping");
                }
            }

        }catch(Exception e){
            if(transaction != null){
                transaction.rollback();;
            }
            e.printStackTrace();
            e.getMessage();

        }
    }
    public static String getJsonStringValue(String[] jsonNames, JsonNode node){
        JsonNode tempNode = node;
        for(int i = 0; i < jsonNames.length; i++){
            if(tempNode != null ){
                tempNode = tempNode.get(jsonNames[i]);
            }else{
                return "";
            }

        }
        if(tempNode.isNull() || tempNode.equals("null")){
            return null;
        }

        return tempNode.asText();
    }

    public static Integer getJsonIntegerValue(String[] jsonNames, JsonNode node){
        JsonNode tempNode = node;
        for(int i = 0; i < jsonNames.length; i++){
            if(tempNode != null){
                tempNode = tempNode.get(jsonNames[i]);
            }else {
                return null;
            }

        }
        if(tempNode.isNull() || tempNode.equals("null")){
            return null;
        }
        return tempNode.asInt();
    }

    public void findLinkableJsons(EntityManager manager,  String jsonFileName, Map<String,List<TempusSample>> otherFileMap, TempusFile tempusFile ){

        String jsonID = jsonFileName.substring(SAMPLE_ID_START,SAMPLE_ID_END).toUpperCase();


        manager.getTransaction().begin();
        List tempusFileEntry = manager.createQuery("Select p.idPerson  " +
                "from TempusFile tf " +
                "JOIN tf.order o " +
                "JOIN tf.patient p where o.accessionId LIKE :jsonID ")
                .setParameter("jsonID", "%" + jsonID + "%")
                .getResultList();
        String idPerson = tempusFileEntry.size() > 0 ? ""+ tempusFileEntry.get(0) : null;
        if(idPerson != null){
            String diagnosis = tempusFile.getPatient().getDiagnosis();
            String fullName = tempusFile.getPatient().getFirstName() + " " + tempusFile.getPatient().getLastName();
            String sex = tempusFile.getPatient().getSex();
            String emr = tempusFile.getPatient().getEmr_id();
            String shadowId = tempusFile.getPatient().getIdBSTShadow();
            String accessionNumber = tempusFile.getOrder().getAccessionId();

            Set<Specimen> specimens = tempusFile.getSpecimens();
            for(Specimen speci : specimens ){
                TempusSample s = new TempusSample();

                String sampleCategory =  speci.getSampleCategory();
                String sampleSite = speci.getSampleSite();
                String sampleType = speci.getSampleType();

                s.setMrn(noEmptyStrDelimiter(emr));
                s.setPersonId(noEmptyStrDelimiter(idPerson));
                s.setFullName(noEmptyStrDelimiter(fullName));
                s.setGender(noEmptyStrDelimiter(sex));
                s.setShadowId(noEmptyStrDelimiter(shadowId));
                s.setTestType(noEmptyStrDelimiter(sampleCategory));
                s.setSampleName(noEmptyStrDelimiter(null)); // unknown right now
                s.setTissueType(noEmptyStrDelimiter(sampleSite));
                s.setSampleSubType(sampleType);
                s.setSubmittedDiagnosis(diagnosis);

                if(!otherFileMap.containsKey(accessionNumber)){
                    List<TempusSample> samples = new ArrayList<>();
                    samples.add(s);
                    otherFileMap.put(accessionNumber, samples);
                }else{
                    otherFileMap.get(accessionNumber).add(s);

                }

            }

            //p.getFullName()


        }


//        List<TempusFile> studs =  manager.createQuery("Select tf from TempusFile tf JOIN r").getResultList();
//        for(Specimen speci  : studs.get(0).getSpecimens()){
//            speci.setNotes("I am the new notes ");
//            manager.persist(speci);
//            break;
//        }

        manager.getTransaction().commit();
    }

    String noEmptyStrDelimiter(Object val){ // add null string as delimited we don't want empty string instead
        if(val == null){
            return "null";
        }
        if(val instanceof String && ((String) val).length() == 0){
            return "null";
        }else{
            return (String)val;
        }
    }


    private boolean isUniqueTempusFile(TempusFile tf, EntityManager manager ){
        String isUniqueTempusFile = "select o.accessionId from TempusFile tf JOIN tf.order o WHERE o.accessionId = :accessionId";
        List order =  manager.createQuery(isUniqueTempusFile)
                .setParameter("accessionId", tf.getOrder().getAccessionId())
                .getResultList();
        if(order.size() > 0 ){
            return false;
        }else{
            return true;
        }
    }

    public void outParseResults(Map<String, List<TempusSample>> otherFileList) throws FileNotFoundException  {
        if(outFileName != null ){
            try(PrintWriter writer = new PrintWriter(outFileName)){
                for(List<TempusSample> samples : otherFileList.values()){
                    for(TempusSample sample : samples){
                        writer.println(sample.toString());
                    }
                }

            }catch (FileNotFoundException e){
                e.printStackTrace();
                throw e;
            }
        }
    }
}
