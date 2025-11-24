package US5;

import dat.entities.*;
import dat.security.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Populator {

    private static EntityManagerFactory emf;

    public Populator(EntityManagerFactory _emf) {
        emf = _emf;
    }

    /**
     * Populate test database with a small but meaningful dataset of users, groups, tags and tickets.
     * Tickets will have minimal messages and optional attachments to verify mappings without overcomplicating tests.
     */
    public void populateDatabase() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // ========== Roles ==========
            Role adminRole = new Role("ADMIN");
            Role agentRole = new Role("AGENT");
            Role userRole = new Role("USER");
            em.persist(adminRole);
            em.persist(agentRole);
            em.persist(userRole);

            // ========== Users ==========
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

            // ========== Groups ==========
            Group supportGroup = new Group();
            supportGroup.setName("Support");
            supportGroup.setDescription("Support group for US5 tests");
            em.persist(supportGroup);

            Group salesGroup = new Group();
            salesGroup.setName("Runner");
            salesGroup.setDescription("Runner group for US5 tests");
            em.persist(salesGroup);

            // ========== Tags ==========
            Tag urgentTag = new Tag();
            urgentTag.setName("urgent");
            em.persist(urgentTag);

            Tag bugTag = new Tag();
            bugTag.setName("bug");
            em.persist(bugTag);

            Tag questionTag = new Tag();
            questionTag.setName("question");
            em.persist(questionTag);

            em.flush();

            // ========== Tickets ==========
            LocalDateTime now = LocalDateTime.now();

            // Ticket 1: Open ticket with one external message, no attachments
            Ticket ticket1 = new Ticket();
            ticket1.setSubject("Test Ticket - Open");
            ticket1.setDescription("Open ticket for US5 tests");
            ticket1.setRequester(customer1);
            ticket1.setAssignee(agent1);
            ticket1.setGroup(supportGroup);
            ticket1.setStatus(Ticket.TicketStatus.OPEN);
            Set<Tag> ticket1Tags = new HashSet<>();
            ticket1Tags.add(urgentTag);
            ticket1Tags.add(bugTag);
            ticket1.setTags(ticket1Tags);
            em.persist(ticket1);

            Message ticket1Message1 = new Message();
            ticket1Message1.setTicket(ticket1);
            ticket1Message1.setAuthor(customer1);
            ticket1Message1.setInternalFlag(false);
            ticket1Message1.setBody("Customer reports an issue with the system.");
            em.persist(ticket1Message1);

            List<Message> ticket1Messages = new ArrayList<>();
            ticket1Messages.add(ticket1Message1);
            ticket1.setMessages(ticket1Messages);

            // Ticket 2: Pending ticket with one internal message and an attachment
            Ticket ticket2 = new Ticket();
            ticket2.setSubject("US1 Test Ticket - Pending");
            ticket2.setDescription("Pending ticket with internal note and attachment");
            ticket2.setRequester(customer2);
            ticket2.setAssignee(agent2);
            ticket2.setGroup(salesGroup);
            ticket2.setStatus(Ticket.TicketStatus.PENDING);
            Set<Tag> ticket2Tags = new HashSet<>();
            ticket2Tags.add(questionTag);
            ticket2.setTags(ticket2Tags);
            em.persist(ticket2);

            Message ticket2Message1 = new Message();
            ticket2Message1.setTicket(ticket2);
            ticket2Message1.setAuthor(agent2);
            ticket2Message1.setInternalFlag(true);
            ticket2Message1.setBody("Internal note: waiting for customer response.");

            Attachment attachment = new Attachment("screenshot.png", "image/png", 12345L, "http://example.com/screenshot.png");
            List<Attachment> attachments = new ArrayList<>();
            attachments.add(attachment);
            ticket2Message1.setAttachments(attachments);

            em.persist(ticket2Message1);

            List<Message> ticket2Messages = new ArrayList<>();
            ticket2Messages.add(ticket2Message1);
            ticket2.setMessages(ticket2Messages);

            // Ticket 3: Solved ticket without messages (to test tickets without conversation)
            Ticket ticket3 = new Ticket();
            ticket3.setSubject("US1 Test Ticket - Solved");
            ticket3.setDescription("Solved ticket without any messages");
            ticket3.setRequester(customer1);
            ticket3.setAssignee(agent2);
            ticket3.setGroup(supportGroup);
            ticket3.setStatus(Ticket.TicketStatus.SOLVED);
            ticket3.setTags(new HashSet<>());
            em.persist(ticket3);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error populating US1 test database: " + e.getMessage());
        }
    }
}
