package GroupTest;

import dat.entities.Group;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Populator {
    private static EntityManagerFactory emf;

    public Populator(EntityManagerFactory _emf) {
        this.emf = _emf;
    }

    public static void populateDatabase() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create test groups
            Group group1 = new Group();
            group1.setName("Support Team");
            group1.setDescription("Customer support and helpdesk team");
            em.persist(group1);

            Group group2 = new Group();
            group2.setName("Development Team");
            group2.setDescription("Software development team");
            em.persist(group2);

            Group group3 = new Group();
            group3.setName("Sales Team");
            group3.setDescription("Sales and business development team");
            em.persist(group3);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error populating database: " + e.getMessage());
        }
    }
}
