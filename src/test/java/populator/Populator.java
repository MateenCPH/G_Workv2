package populator;

import dat.config.HibernateConfig;
import dat.daos.impl.UserDAO;
import dat.dtos.UserDTO;
import dat.entities.User;
import dat.security.daos.SecurityDAO;
import dat.security.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Populator {
    private static EntityManagerFactory emf;
    private static SecurityDAO securityDAO;
    private static UserDAO userDAO;

    public Populator(EntityManagerFactory _emf, UserDAO _userDAO) {
        this.emf = _emf;
        this.userDAO = _userDAO;
        this.securityDAO = new SecurityDAO(_emf);
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

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // ========== Create Roles ==========
            Role adminRole = new Role("ADMIN");
            Role agentRole = new Role("AGENT");
            Role userRole = new Role("USER");
            em.persist(adminRole);
            em.persist(agentRole);
            em.persist(userRole);

            // ========== Create Users ==========
            // 1 Admin
            User admin = new User("admin@gwork.com", "admin123");
            admin.setUsername("admin");
            admin.setFirstName("Admin");
            admin.setLastName("Administrator");
            admin.setActive(true);
            admin.addRole(adminRole);
            em.persist(admin);

            // 2 Agents
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

            // 2 Regular Users
            User user1 = new User("user1@company.com", "user123");
            user1.setUsername("alice_user");
            user1.setFirstName("Alice");
            user1.setLastName("Nielsen");
            user1.setActive(true);
            user1.addRole(userRole);
            em.persist(user1);

            User user2 = new User("user2@company.com", "user123");
            user2.setUsername("bob_user");
            user2.setFirstName("Bob");
            user2.setLastName("Hansen");
            user2.setActive(true);
            user2.addRole(userRole);
            em.persist(user2);

            // ========== Create Groups ==========
            Group itRunner = new Group();
            itRunner.setName("IT-Runner");
            itRunner.setDescription("Handles quick IT support tasks and basic troubleshooting");
            em.persist(itRunner);

            Group itSupporter = new Group();
            itSupporter.setName("IT-Supporter");
            itSupporter.setDescription("Handles advanced technical support and user assistance");
            em.persist(itSupporter);

            Group itDrift = new Group();
            itDrift.setName("IT-Drift");
            itDrift.setDescription("Handles operations, infrastructure, and system maintenance");
            em.persist(itDrift);

            // Add agents to groups
            agent1.getGroups().add(itRunner);
            agent1.getGroups().add(itDrift);
            itRunner.getMembers().add(agent1);
            itDrift.getMembers().add(agent1);

            agent2.getGroups().add(itSupporter);
            itSupporter.getMembers().add(agent2);

            // ========== Create Tags ==========
            Tag runnerTag = new Tag();
            runnerTag.setName("runner");
            em.persist(runnerTag);

            Tag supporterTag = new Tag();
            supporterTag.setName("supporter");
            em.persist(supporterTag);

            Tag driftTag = new Tag();
            driftTag.setName("drift");
            em.persist(driftTag);

            // ========== Create Tickets ==========
            // Ticket 1: Runner tag
            Ticket ticket1 = new Ticket();
            ticket1.setSubject("Printer not working on 3rd floor");
            ticket1.setDescription("The printer in room 312 is not responding. Users cannot print their documents.");
            ticket1.setRequester(user1);
            ticket1.setAssignee(agent1);
            ticket1.setGroup(itRunner);
            ticket1.setStatus(Ticket.TicketStatus.OPEN);
            ticket1.getTags().add(runnerTag);
            em.persist(ticket1);

            // Ticket 2: Supporter tag
            Ticket ticket2 = new Ticket();
            ticket2.setSubject("Need help with software installation");
            ticket2.setDescription("I need to install specialized software for my department but don't have admin rights.");
            ticket2.setRequester(user2);
            ticket2.setAssignee(agent2);
            ticket2.setGroup(itSupporter);
            ticket2.setStatus(Ticket.TicketStatus.PENDING);
            ticket2.getTags().add(supporterTag);
            em.persist(ticket2);

            // Ticket 3: Drift tag
            Ticket ticket3 = new Ticket();
            ticket3.setSubject("Server performance degradation");
            ticket3.setDescription("Our application server has been running slowly since yesterday. Response times are 3x longer than normal.");
            ticket3.setRequester(user1);
            ticket3.setAssignee(agent1);
            ticket3.setGroup(itDrift);
            ticket3.setStatus(Ticket.TicketStatus.OPEN);
            ticket3.getTags().add(driftTag);
            em.persist(ticket3);

            // Ticket 4: Multiple tags (Runner + Supporter)
            Ticket ticket4 = new Ticket();
            ticket4.setSubject("Cannot access shared network drive");
            ticket4.setDescription("When I try to access the shared drive, I get an access denied error.");
            ticket4.setRequester(user2);
            ticket4.setAssignee(agent2);
            ticket4.setGroup(itSupporter);
            ticket4.setStatus(Ticket.TicketStatus.OPEN);
            ticket4.getTags().add(runnerTag);
            ticket4.getTags().add(supporterTag);
            em.persist(ticket4);

            // Ticket 5: Unassigned ticket
            Ticket ticket5 = new Ticket();
            ticket5.setSubject("Email not syncing on mobile device");
            ticket5.setDescription("My work email stopped syncing on my phone this morning.");
            ticket5.setRequester(user1);
            ticket5.setGroup(itRunner);
            ticket5.setStatus(Ticket.TicketStatus.OPEN);
            ticket5.getTags().add(runnerTag);
            em.persist(ticket5);

            // Ticket 6: Solved ticket
            Ticket ticket6 = new Ticket();
            ticket6.setSubject("Password reset request");
            ticket6.setDescription("I forgot my password and need to reset it.");
            ticket6.setRequester(user2);
            ticket6.setAssignee(agent1);
            ticket6.setGroup(itRunner);
            ticket6.setStatus(Ticket.TicketStatus.SOLVED);
            ticket6.getTags().add(runnerTag);
            em.persist(ticket6);

            // ========== Create Messages for Tickets ==========
            // Messages for Ticket 1
            Message msg1_1 = new Message();
            msg1_1.setBody("I've assigned this to the runner team. We'll check the printer shortly.");
            msg1_1.setAuthor(agent1);
            msg1_1.setTicket(ticket1);
            msg1_1.setInternalFlag(false);
            em.persist(msg1_1);

            Message msg1_2 = new Message();
            msg1_2.setBody("Thank you! It's quite urgent as we have a deadline today.");
            msg1_2.setAuthor(user1);
            msg1_2.setTicket(ticket1);
            msg1_2.setInternalFlag(false);
            em.persist(msg1_2);

            // Messages for Ticket 2
            Message msg2_1 = new Message();
            msg2_1.setBody("I'll need to verify the software licensing first. What's the name of the software?");
            msg2_1.setAuthor(agent2);
            msg2_1.setTicket(ticket2);
            msg2_1.setInternalFlag(false);
            em.persist(msg2_1);

            Message msg2_2 = new Message();
            msg2_2.setBody("It's AutoCAD 2024. I've been told our department has licenses available.");
            msg2_2.setAuthor(user2);
            msg2_2.setTicket(ticket2);
            msg2_2.setInternalFlag(false);
            em.persist(msg2_2);

            Message msg2_3 = new Message();
            msg2_3.setBody("[Internal] Need to check with procurement team about license availability.");
            msg2_3.setAuthor(agent2);
            msg2_3.setTicket(ticket2);
            msg2_3.setInternalFlag(true);
            em.persist(msg2_3);

            // Messages for Ticket 3
            Message msg3_1 = new Message();
            msg3_1.setBody("I'm looking into this now. Checking server logs and resource usage.");
            msg3_1.setAuthor(agent1);
            msg3_1.setTicket(ticket3);
            msg3_1.setInternalFlag(false);
            em.persist(msg3_1);

            // Messages for Ticket 6 (Solved)
            Message msg6_1 = new Message();
            msg6_1.setBody("I've reset your password. Check your email for the temporary password.");
            msg6_1.setAuthor(agent1);
            msg6_1.setTicket(ticket6);
            msg6_1.setInternalFlag(false);
            em.persist(msg6_1);

            Message msg6_2 = new Message();
            msg6_2.setBody("Got it, thanks! It works now.");
            msg6_2.setAuthor(user2);
            msg6_2.setTicket(ticket6);
            msg6_2.setInternalFlag(false);
            em.persist(msg6_2);

            // ========== Create Attachments ==========
            Attachment attachment1 = new Attachment(
                "printer_error_screenshot.png",
                "image/png",
                245678,
                "https://storage.gwork.com/attachments/printer_error_screenshot.png"
            );
            msg1_2.getAttachments().add(attachment1);
            em.persist(attachment1);

            Attachment attachment2 = new Attachment(
                "server_performance_log.txt",
                "text/plain",
                15234,
                "https://storage.gwork.com/attachments/server_performance_log.txt"
            );
            msg3_1.getAttachments().add(attachment2);
            em.persist(attachment2);

            em.getTransaction().commit();

            System.out.println("=========================================");
            System.out.println("Test Database Population Complete!");
            System.out.println("=========================================");
            System.out.println("Created:");
            System.out.println("  - 1 Admin: admin@gwork.com (password: admin123)");
            System.out.println("  - 2 Agents: agent1@gwork.com, agent2@gwork.com (password: agent123)");
            System.out.println("  - 2 Users: user1@company.com, user2@company.com (password: user123)");
            System.out.println("  - 3 Groups: IT-Runner, IT-Supporter, IT-Drift");
            System.out.println("  - 3 Tags: runner, supporter, drift");
            System.out.println("  - 6 Tickets with various statuses and assignments");
            System.out.println("  - Multiple messages with 2 attachments");
            System.out.println("=========================================");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error populating database: " + e.getMessage());
        }
    }
}
