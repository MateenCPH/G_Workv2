package US1;

import populator.Populator;
import dat.config.HibernateConfig;
import dat.daos.impl.UserDAO;
import jakarta.persistence.EntityManagerFactory;

class UserDAOTest {

    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private UserDAO userDAO = UserDAO.getInstance(emf);
    Populator populator = new Populator();
}
