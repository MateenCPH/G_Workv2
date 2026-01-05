package dat;

import dat.config.ApplicationConfig;
import dat.config.Populate;

public class Main {

    public static void main(String[] args) {
        ApplicationConfig.startServer(5005);
        Populate.main(args);
        //New testt
        //new test again
//        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
//        PlantDAO dao = PlantDAO.getInstance(emf);
//        dao.readPlantNames().forEach(System.out::println);
    }
}