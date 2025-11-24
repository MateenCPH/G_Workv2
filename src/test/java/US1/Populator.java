package US1;

import dat.entities.User;
import dat.security.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Populator {
    private static EntityManagerFactory emf;

    public Populator(EntityManagerFactory _emf) {
        this.emf = _emf;
    }

    public void populateDatabase() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // ========== Create Roles ==========
            Role adminRole = new Role("ADMIN");
            Role agentRole = new Role("AGENT");
            Role userRole = new Role("USER");
            em.persist(adminRole);
            em.persist(agentRole);
            em.persist(userRole);

            // ========== Create 1 Admin ==========
            User admin = new User("admin@gwork.com", "admin123");
            admin.setUsername("admin");
            admin.setFirstName("Admin");
            admin.setLastName("Administrator");
            admin.setActive(true);
            admin.addRole(adminRole);
            em.persist(admin);

            // ========== Create 2 Agents ==========
            User agent1 = new User("agent1@gwork.com", "agent123");
            agent1.setUsername("runner_agent");
            agent1.setFirstName("Frederik");
            agent1.setLastName("Olfert");
            agent1.setActive(true);
            agent1.addRole(agentRole);
            em.persist(agent1);

            User agent2 = new User("agent2@gwork.com", "agent123");
            agent2.setUsername("support_agent");
            agent2.setFirstName("Anne");
            agent2.setLastName("Brit");
            agent2.setActive(true);
            agent2.addRole(agentRole);
            em.persist(agent2);

            // ========== Create 2 Customers (Users) ==========
            User customer1 = new User("customer1@company.com", "customer123");
            customer1.setUsername("alice_user");
            customer1.setFirstName("Alice");
            customer1.setLastName("Nielsen");
            customer1.setActive(true);
            customer1.addRole(userRole);
            em.persist(customer1);

            User customer2 = new User("customer2@company.com", "customer123");
            customer2.setUsername("bob_user");
            customer2.setFirstName("Bob");
            customer2.setLastName("Hansen");
            customer2.setActive(true);
            customer2.addRole(userRole);
            em.persist(customer2);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error populating database: " + e.getMessage());
        }
    }
}
