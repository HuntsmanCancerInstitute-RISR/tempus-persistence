package hci.ri.tempus.model;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import hci.ri.tempus.util.PeekableScanner;
import javassist.tools.rmi.Sample;

import javax.persistence.*;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class TempusParser {

    private String logFile;
    private String tempusCredFile;
    private String downloadPath;
    private String tempusJsonFileList;
    private String deidentFile;
    private String outFileName;
    private String localDataFile;
    private static final String SCHEMA_VER_ACCEPTED = "1.4";
    private static final Integer SAMPLE_ID_START= 7;
    private static final Integer SAMPLE_ID_END = 13;
    private Map<String,TempusSample> sampleMap;
    private static final Integer nucLen = 4; // ex -RNA
    private ObjectMapper objectMapper;
    private List<String> logList;
    private List<String> newDeidentJsonList;
    public boolean reportOnly;


    public TempusParser(String[] args){
        logList = new ArrayList<>();
        reportOnly = false;
        newDeidentJsonList = new ArrayList<>();


        for(int i = 0; i < args.length; i++){
            args[i] = args[i].toLowerCase();
            if(args[i].equals("-cred")){
                tempusCredFile = args[++i];
            }else if(args[i].equals("-download")){
                downloadPath = args[++i];
            }else if(args[i].equals("-json")){
                tempusJsonFileList = args[++i];
            }else if(args[i].equals("-out")){
                outFileName = args[++i];
            }else if(args[i].equals("-log")) {
                logFile = args[++i];
            }else if(args[i].equals("-ld")){ //local datafile
                localDataFile = args[++i];
            } else if (args[i].equals("-r")) {
                this.reportOnly = true;
            } else if (args[i].equals("-help")) {
                help();
                System.exit(0);
            }
        }

        if(downloadPath == null){
            downloadPath = "";
        }

        if(tempusJsonFileList == null || tempusCredFile == null){
            help();
            System.exit(1);
        }

        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        // this is so we get the default serializer which is needed for backreference/ managed reference support
        module.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (beanDesc.getBeanClass() == HrdFinding.class) {
                    return new HrdFindingDeserializer(deserializer);
                } else if(beanDesc.getBeanClass() == IhcFinding.class) {
                    return new IhcFindingDeserializer(deserializer);
                } else {
                    return deserializer;
                }
            }
        });
        module.addDeserializer(Report.class, new ReportDeserializer());
        module.addDeserializer(Specimen.class, new SpecimenDeserializer());
        module.addDeserializer(Variant.class, new VariantDeserializer());
        module.addDeserializer(SPActionableCPVariant.class, new CopyNumberVariantDeserializer());
        module.addDeserializer(MetaData.class, new MetaDataDeserializer());
        objectMapper.registerModule(module);


        Map<String,String> propMap = new HashMap<String,String>();
        sampleMap = new HashMap<>();
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

    private void addFlaggedSample(Map<String,List<TempusSample>> tempusJsonFileMap,String sampleKey, String sampleName,
                                  TempusSample preBuiltSample){
        List<TempusSample> samples = tempusJsonFileMap.get(sampleKey);

        if(samples == null){ // needs to be added to the map capture flagged item
            samples = new ArrayList<TempusSample>();
            if(preBuiltSample != null){
                samples.add(preBuiltSample);
            }else{
                samples.add(new TempusSample(sampleName));
            }
            tempusJsonFileMap.put(sampleKey, samples);

        }else{
            if(preBuiltSample != null){
                samples.add(preBuiltSample);
            }else{
                samples.add(new TempusSample(sampleName));
            }

        }

    }

    public void parseTempusFileList(List<TempusFile> tempusFileList, Map<String,List<TempusSample>> tempusOtherFileList, EntityManager manager){
        StringBuilder jsonPathPlusFile = new StringBuilder();
        List<String> linkableJsonList = new ArrayList<>();
        PeekableScanner sc = null;

        try{
            sc = new PeekableScanner(new File( tempusJsonFileList));

            while(sc.hasNext()){
                String line = sc.next();
                if(!line.contains("deident.json") && line.contains("json")){
                    System.out.println("JSON being parsed: " + line);
                }
                String pattern = Pattern.quote(System.getProperty("file.separator"));
                String[] fileChunks = getFileChunks(line,pattern);//File.separator);
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


                if(fileType.equals("json") && !fileTypeChunks[fileTypeChunks.length - 2].equals("deident")){
                    TempusFile tFile = parseTempusFile(jsonPathPlusFile.toString(), manager, fileName);
                    findLinkableJsons(manager, fileName, tempusOtherFileList, tFile );
                    scrubJsonFile(tFile,downloadPath + File.separator +line);
                    tempusFileList.add(tFile);

                }else if(fileType.equals("md5") || fileType.equals("pdf") || fileType.equals("json")){
                    //todo not sure if I need to keep track of these
                }
                jsonPathPlusFile.setLength(0);

            }
            if(!reportOnly){
                sc = new PeekableScanner(new File( tempusJsonFileList));
                this.associateMetaDataWithFastqData(tempusOtherFileList, sc, manager);
                sc = new PeekableScanner(new File( localDataFile));
                // look at whole local disc to see if we already imported data
                this.localDataFilter(tempusOtherFileList, sc);

                verifyHasDataWithMeta(tempusOtherFileList);
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

    private void localDataFilter(Map<String, List<TempusSample>> tempusOtherFileList,PeekableScanner sc) {
        while(sc.hasNext()) {

            String line = sc.next();
            //all fastq/md5s files will be in DNA OR RNA folder
            String pattern = Pattern.quote(System.getProperty("file.separator"));
            String[] fileChunks = getFileChunks(line, pattern);//File.separator);
            String fileName = fileChunks[fileChunks.length - 1];
            if (!fileName.endsWith("fastq.gz") && !fileName.endsWith("fastq.gz.S3.txt")  ) {
                continue;
            }

            String[] chunks = fileName.split("_");
            List<String> chunkList = new ArrayList<String>(Arrays.asList(chunks));
            String fileTestType = "";
            String nucType = "";
            String excludeNucFilename = chunks[0];

            if(chunks[0].endsWith("DNA") || chunkList.stream().anyMatch(c -> c.contains("DSQ"))){
                nucType = "DNA";
                if(chunks[0].endsWith("DNA")){
                    excludeNucFilename = chunks[0].substring(0, chunks[0].length() - nucLen);
                }
            } else if(chunks[0].endsWith("RNA") ||  chunkList.stream().anyMatch(c -> c.contains("RSQ"))){
                nucType = "RNA";
                if(chunks[0].endsWith("RNA")){
                    excludeNucFilename = chunks[0].substring(0, chunks[0].length() - nucLen);
                }
            }


            List<TempusSample> samples = tempusOtherFileList.get(excludeNucFilename);

            String sampleName = String.join("_", Arrays.copyOfRange(chunks, 0, chunks.length - 1));
            TempusSample rnaSample = new TempusSample();
            // we only are check fastq files missing  that are DNA based 'result as sample name only happens in that case'
            if ((nucType.equals("DNA")) && chunkList.contains("N")) {
                fileTestType = "normal";

            } else if ((nucType.equals("DNA")) && chunkList.contains("T")) {
                fileTestType = "tumor";
            }

            List<TempusSample> removedSamples = new ArrayList<>();
            if(samples != null){
                for(int i = 0; i < samples.size(); i++){
                    TempusSample s = samples.get(i);
                    if(!s.getSampleName().equals(sampleName) && s.getTestType().equals(fileTestType)){
                        if(s.getSampleName().startsWith("result") && !nucType.equals("RNA")){
                            System.out.println( "Found " + sampleName + " " + fileTestType + " on local disk will not flag");
                            s.setSampleName(sampleName);
                        }
                    }
                }

            }


        }
    }


    private void associateMetaDataWithFastqData(Map<String, List<TempusSample>> tempusOtherFileList, PeekableScanner sc,
                                                EntityManager manager){

        boolean foundRNA = false; // RNA seq
        boolean foundTumor = false;
        boolean foundNormal = false;
        Map<String,String> sampleNameMap = new HashMap<String,String>();

        while(sc.hasNext()){

            String line = sc.next();

            //all fastq/md5s files will be in DNA OR RNA folder
            String pattern = Pattern.quote(System.getProperty("file.separator"));
            String[] fileChunks = getFileChunks(line,pattern);//File.separator);
            String fileName = fileChunks[fileChunks.length - 1];
            if( !fileName.endsWith("fastq.gz")){
                continue;
            }


            String[] chunks =  fileName.split("_");
            List<String> chunkList = new ArrayList<String>(Arrays.asList(chunks));
            String fileTestType = "";

            String nucType =  fileChunks[0];// chunks[0].substring(chunks[0].length() - nucLen + 1);
            int nucIdx = chunks[0].indexOf(nucType);
            String excludeNucFilename = chunks[0];
            if(nucIdx > 0 ){
                // if DNA or RNA found we assume it is at the end since previous script follows that standard
                excludeNucFilename = chunks[0].substring(0, chunks[0].length() - nucLen);
            }

            List<TempusSample> samples  = tempusOtherFileList.get(excludeNucFilename);

            String sampleName = String.join("_", Arrays.copyOfRange(chunks, 0, chunks.length - 1) );
            TempusSample rnaSample = new TempusSample();
            //search the database just incase json file got moved prematurely

            if(samples == null){
                samples = findImportedJson(manager, excludeNucFilename);
                if(samples != null && samples.size() > 0){
                    tempusOtherFileList.put(excludeNucFilename, samples);
                }
            }

            // match file name with tissue type sample category(tumor, normal)
            if((nucType.equals("DNA") || nucType.equals("DSQ1"))  && chunkList.contains("N")){
                fileTestType = "normal";

            }else if((nucType.equals("DNA") || nucType.equals("DSQ1")) && chunkList.contains("T")){
                fileTestType = "tumor";
            }else if(nucType.equals("RNA") || (chunkList.contains("RSQ1") || chunkList.contains("RS")) ){
                fileTestType = "RS";
                foundRNA = true; // don't know if tempus provides rna seq sample meta data
                rnaSample.setSampleName(sampleName);
                rnaSample.setTestType("tumor");
                if(samples != null && samples.size() > 0 ){
                    rnaSample.setPersonId(samples.get(0).getPersonId()); // should always be atleast one entry in array for its key
                    rnaSample.setMrn(samples.get(0).getMrn());
                    rnaSample.setGender(samples.get(0).getGender());
                    rnaSample.setFullName(samples.get(0).getFullName());
                }

            }else{
                System.out.println("Error: Couldn't associate fastq filename with kind of tissue type " + fileName);
                logList.add("Error: Naming convention change, Couldn't associate fastq filename with kind of tissue type. " + fileName);
                System.exit(1);
            }
            // if sample metadata exists but not the file then the sampleName never gets set and gets flagged
            if(samples != null){
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
            }

            // need to pair filetype to filename since the code below is only ran once all files for that given file id
            sampleNameMap.put(fileTestType,sampleName);

            if(isNewPatientFile(sc, chunks,excludeNucFilename)){
                if(foundRNA){
                    String sName = sampleNameMap.get("RS");
                    if(sName != null){ // this doesn't mean its flagged if it has all criteria
                        addFlaggedSample(tempusOtherFileList,excludeNucFilename, sName,rnaSample);
                    }
                }

                if(!foundNormal){
                    String sName = sampleNameMap.get("normal");
                    String displayName = sName != null ? sName : excludeNucFilename;

                    if(sName != null){
                        // this is the case where there is a file but no sample metadata for it
                        // this is how we get it flagged in the next step because its missing idPerson
                        System.out.println("FLAGGING: didn't find the metadata for normal of sample " + displayName );
                        logList.add("FLAGGING: didn't find the metadata for normal of sample " + displayName);
                        addFlaggedSample(tempusOtherFileList,excludeNucFilename, sName,null);
                    }else {
                        System.out.println("WARNING: didn't find the fastq file for normal of sample " + displayName );
                        logList.add("WARNING: didn't find fastq file for  normal of sample " + displayName);
                    }

                }
                if(!foundTumor){
                    String sName = sampleNameMap.get("tumor");
                    String displayName = sName != null ? sName : excludeNucFilename;

                    if(sName != null){
                        // this is the case where there is a file but no sample metadata for it
                        // this is how we get it flagged in the next step because its missing idPerson
                        System.out.println("FLAGGING: didn't find the metadata for tumor of sample " + displayName);
                        logList.add("FLAGGING: didn't find the metadata for tumor of sample " + displayName);
                        addFlaggedSample(tempusOtherFileList,excludeNucFilename, sName,null);
                    }else {
                        System.out.println("WARNING: didn't find the fastq file for tumor of sample " + displayName );
                        logList.add("WARNING: didn't find fastq file for  tumor of sample " + displayName);
                    }
                }
                foundRNA = false;
                foundTumor = false;
                foundNormal = false;
                sampleNameMap.clear();
            }

            //tempusOtherFileList.put(,jsonPathPlusFile.toString());
        }

    }



    // make sure json has fastq data with it. If not flagged it by the name of the json
    private void verifyHasDataWithMeta(Map<String, List<TempusSample>> metaFileList) {
        for(Map.Entry<String,List<TempusSample>> entry : metaFileList.entrySet()){
            String key = entry.getKey();
            ArrayList<TempusSample> samples = (ArrayList<TempusSample>)entry.getValue();
            for(TempusSample tSample : samples){
                if(tSample.getSampleName().startsWith("result")){ // if json naming convention  changes you will need to update this
                    // flagging because no tempus data was found
                    if(!tSample.getPersonId().equals("-1")){
                        System.out.println("WARNING: didn't find potentially all fastq files for this json: " + tSample.getSampleName());
                        logList.add("WARNING: didn't find potentially all fastq files for this json: " + tSample.getSampleName());
                        tSample.setPersonId(null);
                    }else{
                        System.out.println("WARNING: this sample doesn't have a hci person id: " + tSample.getSampleName());
                        logList.add("WARNING: this sample doesn't have a hci person id: " + tSample.getSampleName());
                        tSample.setPersonId(null);
                    }

                }
            }
        }
    }

    public boolean isNewPatientFile(PeekableScanner sc,String[] chunks,String excludeNucFileName){
        boolean isNewPatientFile = false;
        String nextLine = sc.peek();
        if(nextLine != null){
            String pattern = Pattern.quote(System.getProperty("file.separator"));
            String[] pathChunks = getFileChunks(nextLine,pattern);// "\\"+File.separator);
            String nextFile = pathChunks[ pathChunks.length - 1];
            String[] nextChunks = nextFile.split("_");

            int nucIdx = nextChunks[0].indexOf(pathChunks[0]);
            String nextExcludeNucFileName = nextChunks[0];
            if(nucIdx > 0 ) {
                nextExcludeNucFileName = nextChunks[0].substring(0, chunks[0].length() - nucLen);
            }


            // fastq.gz not fastq.gz.md5
            // todo if fastq is not the only file type in the future you might need to improve strategy
            if(!nextFile.endsWith("fastq.gz") ||  !excludeNucFileName.equals(nextExcludeNucFileName)){
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
        String dob = tFile.getPatient().getDateOfBirth();
        String firstName =  tFile.getPatient().getFirstName();
        String lastName = tFile.getPatient().getLastName();
        String tempusId = tFile.getPatient().getTempusId();
        String emrId = tFile.getPatient().getEmrId();

        String[] fileChunks = name.split("\\.");
        if(fileChunks[fileChunks.length - 1].equals("json")){
            String nameNoExt = fileChunks[0];
            String deidentJson = path + File.separator +nameNoExt + ".deident.json";
            File deidentJsonFile = new File(deidentJson);
            if(deidentJsonFile.exists()) {
                return;
            }
            tFile.getPatient().setDateOfBirth("xxx");
            tFile.getPatient().setFirstName("xxx");
            tFile.getPatient().setLastName("xxx");
            tFile.getPatient().setTempusId("xxx");
            tFile.getPatient().setEmrId("xxx");
            newDeidentJsonList.add( nameNoExt +".deident.json");
            System.out.println("about to scrub " + nameNoExt+ ".json" );
            writer.writeValue(new File(path + File.separator  + nameNoExt + ".deident.json"),tFile);
        }
        tFile.getPatient().setDateOfBirth(dob);
        tFile.getPatient().setFirstName(firstName);
        tFile.getPatient().setLastName(lastName);
        tFile.getPatient().setTempusId(tempusId);
        tFile.getPatient().setEmrId(emrId);

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


    public TempusFile parseTempusFile(String tempusJson, EntityManager manager, String jsonFileName) throws Exception{
        String jsonID = jsonFileName.substring(SAMPLE_ID_START,SAMPLE_ID_END).toUpperCase();
        byte[] jsonData = Files.readAllBytes(Paths.get(tempusJson));
        TempusFile tf = objectMapper.readValue(jsonData, TempusFile.class);
        if(!tf.getMetadata().getSchemaVersion().equals(SCHEMA_VER_ACCEPTED)){
            System.out.println("JSON schema version " + tf.getMetadata().getSchemaVersion()
                    + " is incompatible with parser accepting " + SCHEMA_VER_ACCEPTED + " for " + tf.getOrder().getAccessionId());
            logList.add("WARNING: JSON schema version " +
                    tf.getMetadata().getSchemaVersion() + " is incompatible with parser accepting " + SCHEMA_VER_ACCEPTED + " for " + tf.getOrder().getAccessionId()  );

            //throw new Exception("JSON schema version is incompatible with parser ");
        }
        //todo need to make the parser support updating a patient
        //todo can't figure a way to do this because json doesn't provide unique ids for every entity therefore it may mess up an update

//        List tempusFileEntry = manager.createQuery("SELECT tf.idTempusFile FROM TempusFile tf JOIN tf.order as o " +
//                "WHERE o.accessionId LIKE :jsonID")
//                .setParameter("jsonID", "%" + jsonID + "%")
//                .getResultList();
//        Long idTempusFile = tempusFileEntry.size() > 0 ? (Long) tempusFileEntry.get(0) : null;
//        if(idTempusFile != null){
//            EntityTransaction transaction= manager.getTransaction();
//            transaction.begin();
//            TempusFile tf1 = manager.find(TempusFile.class,idTempusFile);
//            tf1.getSpecimens().forEach((Specimen sp) -> {
//                sp.setNotes("I am updating you notes");
//            });
//        tf.getOrder().setIdOrder(tf1.getOrder().getIdOrder());
//        tf.getReport().setIdReport(tf1.getReport().getIdReport());
//        tf.getPatient().setIdPatient(tf1.getPatient().getIdPatient());
//
//
//            manager.merge(tf);
//            transaction.commit();

        //}


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
                    transaction = manager.getTransaction();
//                    TempusFile mTf = manager.find(TempusFile.class,  new Long(1583));
//                    transaction.begin();
//                    manager.remove(mTf);
//                    transaction.commit();
                    System.out.println("This json has already been imported for " + tf.getOrder().getAccessionId()+ "... Skipping");
                    logList.add("This json has already been imported for " + tf.getOrder().getAccessionId()+ "... Skipping");
                }
            }

        }catch(Exception e){
            if(transaction != null){
                transaction.rollback();;
            }
            e.printStackTrace();
            e.getMessage();
            System.exit(1);

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
        if(tempNode == null || tempNode.isNull() || tempNode.equals("null")){
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
        if(tempNode == null || tempNode.isNull() || tempNode.equals("null")){
            return null;
        }
        return tempNode.asInt();
    }
    public static Double getJsonDoubleValue(String[] jsonNames, JsonNode node){
        JsonNode tempNode = node;
        for(int i = 0; i < jsonNames.length; i++){
            if(tempNode != null){
                tempNode = tempNode.get(jsonNames[i]);
            }else {
                return null;
            }

        }
        if(tempNode == null || tempNode.isNull() || tempNode.equals("null")){
            return null;
        }
        return tempNode.asDouble();
    }
    public List<TempusSample> findImportedJson(EntityManager manager, String accessionId){
        List<TempusSample> samples = new ArrayList<>();
        List tempusFileEntry = manager.createQuery("SELECT lp.idTempusFile, lp.HCIPersonID FROM TempusLinkedPatient lp " +
                        "WHERE lp.accessionId LIKE :accessionId")
                .setParameter("accessionId", "%" + accessionId + "%")
                .getResultList();
        Long idTempusFile = null;
        Integer hciPersonId = null;
        if(tempusFileEntry.size() == 1){
            idTempusFile = (Long)((Object[])tempusFileEntry.get(0))[0];
            hciPersonId = (Integer) ((Object[])tempusFileEntry.get(0))[1];
        } else if(tempusFileEntry.size() > 1) {
            System.out.println("Error: the following Accession ID: " + accessionId + " should be unique but is not");
            logList.add("Error: the following accession ID: " + accessionId + " should be unique but is not");
            //System.exit(1);
        }

        if(idTempusFile != null) {
            TempusFile tf = manager.find(TempusFile.class, idTempusFile);
            if(hciPersonId != null){
                String diagnosis = tf.getPatient().getDiagnosis();
                String fullName = tf.getPatient().getFirstName() + " " + tf.getPatient().getLastName();
                String sex = tf.getPatient().getSex();
                String emr = tf.getPatient().getEmrId();
                String shadowId = tf.getPatient().getIdBSTShadow();

                Set<Specimen> specimens = tf.getSpecimens();
                String cpTestName = tf.getOrder().getTest().getName();
                String cpDesign = tf.getOrder().getTest().getCode();
                String cpDescription = tf.getOrder().getTest().getDescription();

                for(Specimen speci : specimens ){
                    TempusSample s = new TempusSample();

                    String sampleCategory =  speci.getSampleCategory();
                    String sampleSite = speci.getSampleSite();
                    String sampleType = speci.getSampleType();
                    String sampleName = tf.getJsonId() != null && !tf.getJsonId().equals("") ? tf.getJsonId() : "result_"+ tf.getReport().getReportId();

                    s.setMrn(noEmptyStrDelimiter(emr));
                    s.setPersonId(noEmptyStrDelimiter("" + hciPersonId));
                    s.setFullName(noEmptyStrDelimiter(fullName));
                    s.setGender(noEmptyStrDelimiter(sex));
                    s.setShadowId(noEmptyStrDelimiter(shadowId));
                    s.setTestType(noEmptyStrDelimiter(sampleCategory));
                    s.setSampleName(noEmptyStrDelimiter(sampleName)); // unknown right now
                    s.setTissueType(noEmptyStrDelimiter(sampleSite));
                    s.setSampleSubType(noEmptyStrDelimiter(sampleType));
                    s.setSubmittedDiagnosis(noEmptyStrDelimiter(diagnosis));
                    //duplicate info, will allow because tumor/normal aren't consistently present
                    s.setCaptureTestName(noEmptyStrDelimiter(cpTestName));
                    s.setCaptureDesign(noEmptyStrDelimiter(cpDesign));
                    s.setCaptureDescription(noEmptyStrDelimiter(cpDescription));
                    samples.add(s);
                }

            }
        }
        return samples.size() > 0 ? samples : null;

    }
    public void reportPairJsonFastq(Map<String, List<TempusSample>> tempusOtherFileList,
                                    EntityManager manager) {
        PeekableScanner sc = null;

        try {
            sc = new PeekableScanner(new File(tempusJsonFileList));
            String prevAccessionID = "";
            logList.clear();
            StringBuilder strBuilder = new StringBuilder();
            while(sc.hasNext()) {

                String line = sc.next();

                //all fastq/md5s files will be in DNA OR RNA folder
                String pattern = Pattern.quote(System.getProperty("file.separator"));
                String[] fileChunks = getFileChunks(line, pattern);//File.separator);
                String fileName = fileChunks[fileChunks.length - 1];
                if (!fileName.endsWith("fastq.gz")) {
                    continue;
                }


                String[] chunks = fileName.split("_");
                List<String> chunkList = new ArrayList<String>(Arrays.asList(chunks));
                String fileTestType = "";

                String nucType = fileChunks[0];// chunks[0].substring(chunks[0].length() - nucLen + 1);
                int nucIdx = chunks[0].indexOf(nucType);
                String excludeNucFilename = chunks[0];
                if (nucIdx > 0) {
                    // if DNA or RNA found we assume it is at the end since previous script follows that standard
                    excludeNucFilename = chunks[0].substring(0, chunks[0].length() - nucLen);
                }


                List<TempusSample> samples = tempusOtherFileList.get(excludeNucFilename);
                boolean isStart = !prevAccessionID.equals(excludeNucFilename);

                if(samples != null  && samples.size() > 0){
                    if(isStart){
                        sampleMap.remove(samples.get(0).getSampleName());
                        if(!prevAccessionID.equals("")){
                            logList.add(strBuilder.toString());
                        }
                        prevAccessionID = excludeNucFilename;
                        if(!samples.get(0).getPersonId().equals( "null")){
                            strBuilder = new StringBuilder("PI (");
                            strBuilder.append(samples.get(0).getPersonId());
                        }else {
                            strBuilder = new StringBuilder("MRN (");
                            strBuilder.append(samples.get(0).getMrn());
                        }

                        strBuilder.append(")");
                        strBuilder.append(samples.get(0).getSampleName());
                        strBuilder.append(": ");
                        strBuilder.append(fileName);
                        strBuilder.append(" ");

                    }else {
                        strBuilder.append(fileName);
                        strBuilder.append(" ");
                    }
                }else {
                    if(isStart){ // is start of new fastq
                        prevAccessionID = excludeNucFilename;
                        logList.add(strBuilder.toString());
                        strBuilder = new StringBuilder("Unpaired no json: ");
                        strBuilder.append(excludeNucFilename);
                    }
                }


            }
            if(strBuilder.length() > 0){
                logList.add(strBuilder.toString());
            }
            strBuilder = new StringBuilder();
            for(Map.Entry<String,TempusSample> entry : sampleMap.entrySet() ){
                TempusSample tSample = entry.getValue();
                strBuilder.append("Unpaired no fastq");
                if(tSample.getPersonId().equals("null")){
                    strBuilder.append(" MRN(");
                    strBuilder.append(tSample.getMrn());
                }else {
                    strBuilder.append(" PI(");
                    strBuilder.append(tSample.getPersonId());
                }

                strBuilder.append(") ");
                strBuilder.append(tSample.getSampleName());
                logList.add(strBuilder.toString());
                strBuilder = new StringBuilder();
            }
            Collections.sort(this.logList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            sc.close();
        }finally {
            sc.close();
        }

    }

    public void findLinkableJsons(EntityManager manager,  String jsonFileName, Map<String,List<TempusSample>> otherFileMap, TempusFile tempusFile ){
        // getting just the id that is shared between the fastq file and the json id
        String jsonID = jsonFileName.substring(SAMPLE_ID_START,SAMPLE_ID_END).toUpperCase();
        String jsonNameOnly = jsonFileName.split("\\.")[0];
        tempusFile.setJsonId(jsonNameOnly);

        manager.getTransaction().begin();

        List tempusFileEntry = new ArrayList();
        String hciPersonID = null;

        tempusFileEntry = manager.createQuery("SELECT p.HCIPersonID FROM TempusLinkedPatient p " +
                        "WHERE p.accessionId LIKE :accessionID")
                .setParameter("accessionID", "%" + tempusFile.getOrder().getAccessionId() + "%")
                .getResultList();
        hciPersonID = tempusFileEntry.size() > 0 ? ""+ tempusFileEntry.get(0) : null;


        if(hciPersonID != null || reportOnly){
            String diagnosis = tempusFile.getPatient().getDiagnosis();
            String fullName = tempusFile.getPatient().getFirstName() + " " + tempusFile.getPatient().getLastName();
            String sex = tempusFile.getPatient().getSex();
            String emr = tempusFile.getPatient().getEmrId();
            String shadowId = tempusFile.getPatient().getIdBSTShadow();
            String accessionNumber = tempusFile.getOrder().getAccessionId();
            System.out.println("This is the emr value: " + emr + " for accession number: " + accessionNumber );
            System.out.println("This is the hci person id value: " +  hciPersonID + " for accession number: " + accessionNumber );

            Set<Specimen> specimens = tempusFile.getSpecimens();
            String cpTestName = tempusFile.getOrder().getTest().getName();
            String cpDesign = tempusFile.getOrder().getTest().getCode();
            String cpDescription = tempusFile.getOrder().getTest().getDescription();

            for(Specimen speci : specimens ){
                TempusSample s = new TempusSample();

                String sampleCategory =  speci.getSampleCategory();
                String sampleSite = speci.getSampleSite();
                String sampleType = speci.getSampleType();

                s.setMrn(noEmptyStrDelimiter(emr));
                s.setPersonId(noEmptyStrDelimiter(hciPersonID));
                s.setFullName(noEmptyStrDelimiter(fullName));
                s.setGender(noEmptyStrDelimiter(sex));
                s.setShadowId(noEmptyStrDelimiter(shadowId));
                s.setTestType(noEmptyStrDelimiter(sampleCategory));
                s.setSampleName(noEmptyStrDelimiter(jsonNameOnly)); // unknown right now
                s.setTissueType(noEmptyStrDelimiter(sampleSite));
                s.setSampleSubType(noEmptyStrDelimiter(sampleType));
                s.setSubmittedDiagnosis(noEmptyStrDelimiter(diagnosis));
                //duplicate info, will allow because tumor/normal aren't consistently present
                s.setCaptureTestName(noEmptyStrDelimiter(cpTestName));
                s.setCaptureDesign(noEmptyStrDelimiter(cpDesign));
                s.setCaptureDescription(noEmptyStrDelimiter(cpDescription));



                if(!otherFileMap.containsKey(accessionNumber)){
                    List<TempusSample> samples = new ArrayList<>();
                    samples.add(s);
                    otherFileMap.put(accessionNumber, samples);
                }else{
                    otherFileMap.get(accessionNumber).add(s);

                }
                if(reportOnly){
                    sampleMap.put(jsonNameOnly, s);
                }

            }

        }else {
            System.out.println("Warning: No hci person id can be found for this these files "
                    + tempusFile.getOrder().getAccessionId() + " AND "  + jsonFileName + ". The emr id is: " + tempusFile.getPatient().getEmrId() );
            logList.add("Warning: No hci person id can be found for this these files "
                    + tempusFile.getOrder().getAccessionId() + " AND "  + jsonFileName + ". The emr id is: " + tempusFile.getPatient().getEmrId() );

            TempusSample s = new TempusSample();
            s.setSampleName(jsonNameOnly);
            s.setPersonId("-1"); // a marker to show that hci person id is missing not being explicitly set to null for flagging purposes
            otherFileMap.put(jsonNameOnly,new ArrayList<TempusSample>(Arrays.asList(s)));
        }



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
        if(order.size() > 0){
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

    public void saveLogFile() throws FileNotFoundException {
        if(reportOnly && logFile != null){
            logFile = logFile.split("\\.")[0] + "Report.txt";
        }

        if(logFile != null ){
            try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile,!reportOnly)))){
                for(String log : logList){
                    writer.println(log);
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
                throw e;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDeidentJsonToFileList() throws FileNotFoundException {
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(tempusJsonFileList,true)))){
            for(int i = 0; i < newDeidentJsonList.size(); i++){
                writer.println(newDeidentJsonList.get(i));
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
