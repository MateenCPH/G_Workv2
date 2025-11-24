package dat.config;

import dat.entities.*;
import dat.security.entities.Role;
import dat.entities.User;
import jakarta.persistence.EntityManagerFactory;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class Populate {
    public static void main(String[] args) {
        populateDatabase();
        //clearDatabase();
    }

    public static void populateDatabase() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create Roles
            Role adminRole = new Role("ADMIN");
            Role userRole = new Role("USER");
            Role agentRole = new Role("AGENT");
            em.persist(adminRole);
            em.persist(userRole);
            em.persist(agentRole);

            // Create Users
            User admin = new User("admin@example.com", "admin123");
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setUsername("admin");
            admin.addRole(adminRole);
            em.persist(admin);

            User agent1 = new User("agent1@example.com", "agent123");
            agent1.setFirstName("Frederik");
            agent1.setLastName("IT-Runner");
            agent1.setUsername("agent1");
            agent1.addRole(agentRole);
            em.persist(agent1);

            User agent2 = new User("agent2@example.com", "agent123");
            agent2.setFirstName("Anne-Brit");
            agent2.setLastName("Support");
            agent2.setUsername("agent2");
            agent2.addRole(agentRole);
            em.persist(agent2);

            User user1 = new User("user1@example.com", "user123");
            user1.setFirstName("Alice");
            user1.setLastName("Customer");
            user1.setUsername("user1");
            user1.addRole(userRole);
            em.persist(user1);

            User user2 = new User("user2@example.com", "user123");
            user2.setFirstName("Bob");
            user2.setLastName("Customer");
            user2.setUsername("user2");
            user2.addRole(userRole);
            em.persist(user2);

            // Create Groups
            Group itRunner = new Group();
            itRunner.setName("IT-Runner");
            itRunner.setDescription("Handles technical issues");
            itRunner.getMembers().add(agent1);
            em.persist(itRunner);

            Group supporter = new Group();
            supporter.setName("Support");
            supporter.setDescription("Handles advanced support issues");
            supporter.getMembers().add(agent2);
            em.persist(supporter);

            // Create Tags
            Tag runners = new Tag();
            runners.setName("runners");
            em.persist(runners);

            Tag support = new Tag();
            support.setName("support");
            em.persist(support);

            Tag indkoeb = new Tag();
            indkoeb.setName("indkøb");
            em.persist(indkoeb);

            // Create Tickets
            Ticket ticket1 = new Ticket();
            ticket1.setSubject("Cannot login to account");
            ticket1.setDescription("I've been trying to login for the past hour but keep getting error messages.");
            ticket1.setRequester(user1);
            ticket1.setAssignee(agent1);
            ticket1.setGroup(itRunner);
            ticket1.setStatus(Ticket.TicketStatus.OPEN);
            ticket1.getTags().add(runners);
            ticket1.getTags().add(indkoeb);
            em.persist(ticket1);

            Ticket ticket2 = new Ticket();
            ticket2.setSubject("Feature request: Dark mode");
            ticket2.setDescription("Would love to have a dark mode option for the application.");
            ticket2.setRequester(user2);
            ticket2.setAssignee(agent2);
            ticket2.setGroup(itRunner);
            ticket1.setStatus(Ticket.TicketStatus.PENDING);
            ticket2.getTags().add(support);
            em.persist(ticket2);

            Ticket ticket3 = new Ticket();
            ticket3.setSubject("Billing discrepancy");
            ticket3.setDescription("I was charged twice for my subscription this month.");
            ticket3.setRequester(user1);
            ticket3.setAssignee(agent1);
            ticket3.setGroup(supporter);
            ticket1.setStatus(Ticket.TicketStatus.SOLVED);
            ticket3.getTags().add(runners);
            em.persist(ticket3);

            Ticket ticket4 = new Ticket();
            ticket4.setSubject("App crashes on startup");
            ticket4.setDescription("The application crashes immediately after opening.");
            ticket4.setRequester(user2);
            ticket1.setStatus(Ticket.TicketStatus.OPEN);
            ticket4.getTags().add(runners);
            ticket4.getTags().add(indkoeb);
            em.persist(ticket4);

            // Create Messages for tickets
            Message message1 = new Message();
            message1.setBody("I've started investigating this issue. Can you provide more details about the error message?");
            message1.setAuthor(agent1);
            message1.setTicket(ticket1);
            em.persist(message1);

            Message message2 = new Message();
            message2.setBody("The error says 'Invalid credentials' but I'm sure my password is correct.");
            message2.setAuthor(user1);
            message2.setTicket(ticket1);
            em.persist(message2);

            Message message3 = new Message();
            message3.setBody("Thank you for the suggestion! We'll add it to our roadmap.");
            message3.setAuthor(agent2);
            message3.setTicket(ticket2);
            em.persist(message3);

            em.getTransaction().commit();

            System.out.println("Database populated successfully with users, tickets, groups, tags, and messages!");
        }
    }

    public static void clearDatabase() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.createQuery("DELETE FROM Message").executeUpdate();
            em.createQuery("DELETE FROM Ticket").executeUpdate();
            em.createQuery("DELETE FROM Tag").executeUpdate();
            em.createQuery("DELETE FROM Group").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();

            em.getTransaction().commit();

            System.out.println("Database cleared successfully!");
        }
    }
}

