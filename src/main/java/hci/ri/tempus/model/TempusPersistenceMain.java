package hci.ri.tempus.model;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;


public class TempusPersistenceMain {
//    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
//            .createEntityManagerFactory("tempus");

    private static int status;




    public static void main(String[] args) throws IOException {

        EntityManager manager = null;
        try{

            TempusParser tParser = new TempusParser(args);

            final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
                    .createEntityManagerFactory("tempus",  tParser.loadCred());
            manager = ENTITY_MANAGER_FACTORY.createEntityManager();


            //findTempusFile(manager);
            List<TempusFile> tempFileList = new ArrayList<TempusFile>();
            Map<String,List<TempusSample>> tempOtherFileList = new HashMap<String,List<TempusSample>>();
            tParser.parseTempusFileList(tempFileList,tempOtherFileList, manager);
            //tParser.importTempusFile(manager,tempFileList,tempOtherFileList);





        }catch (Exception e){
            e.getMessage();
            e.printStackTrace();
            status = 1;
        }finally {
            if(manager != null){
                manager.close();
            }
            if(status > 0){
                System.exit(status);
            }
        }

    }


    public static void findTempusFile(EntityManager manager){

        String sex = "Male";
        manager.getTransaction().begin();
        TempusFile firstOne = manager.find(TempusFile.class,new Long(1));
        List studs = manager.createQuery("Select r  from TempusFile tf JOIN tf.results r JOIN tf.patient p where p.sex = :sex ")
                .setParameter("sex", sex)
                .getResultList();

//        List<TempusFile> studs =  manager.createQuery("Select tf from TempusFile tf JOIN r").getResultList();
//        for(Specimen speci  : studs.get(0).getSpecimens()){
//            speci.setNotes("I am the new notes ");
//            manager.persist(speci);
//            break;
//        }
//        manager.persist(studs.get(0));
        manager.getTransaction().commit();
    }










}