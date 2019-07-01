package hci.ri.tempus.model;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;

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
    private static final String SCHEMA_VER_ACCEPTED = "1.3";
    private static final Integer SAMPLE_ID_START= 7;
    private static final Integer SAMPLE_ID_END = 13;
    private Map<String,Specimen> specimenMap;
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

        try (Scanner sc = new Scanner(new File( tempusJsonFileList))) {
            while(sc.hasNextLine()){
                String jsonLine = sc.nextLine();
                System.out.println(File.separator);
                String[] fileChunks = getFileChunks(jsonLine,"\\"+File.separator);
                String path = "";
                String fileName = fileChunks[fileChunks.length - 1];
                String fileType = getFileChunks(fileName, "\\.")[1];

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
                    findLinkableJsons(manager, fileName, tempusOtherFileList );
                    tempusFileList.add(tFile);

                }else {
                    fileName.split("_");

                    //tempusOtherFileList.put(,jsonPathPlusFile.toString());
                }
                jsonPathPlusFile.setLength(0);


            }


        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
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
    public  void importTempusFile(EntityManager manager, List<TempusFile> tfList, Map<String,String> tempOtherFileList ){
        EntityTransaction transaction = null;
        try{
            for(TempusFile tf : tfList){
                System.out.println("Now importing " + tf.getOrder().getAccessionId());
                transaction= manager.getTransaction();
                transaction.begin();
                manager.persist(tf);
                transaction.commit();



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

    public void findLinkableJsons(EntityManager manager,  String jsonFileName, Map<String,List<TempusSample>> otherFileMap ){

        String jsonID = jsonFileName.substring(SAMPLE_ID_START,SAMPLE_ID_END).toUpperCase();


        manager.getTransaction().begin();
        List tempusFileEntry = manager.createQuery("Select p.idPerson, o.accessionId,  p.emr_id, p.idBSTShadow  " +
                "from TempusFile tf " +
                "JOIN tf.order o " +
                "JOIN tf.patient p where o.accessionId LIKE :jsonID ")
                .setParameter("jsonID", "%" + jsonID + "%")
                .getResultList();
        Object[] tArray = tempusFileEntry.size() > 0 ? (Object[])tempusFileEntry.get(0) : null;
        if(tArray != null && tArray[0] != null){
            TempusSample p = new TempusSample();
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









}
