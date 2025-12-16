package dat.config;

import dat.entities.*;
import dat.security.entities.Role;
import dat.entities.User;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;

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

            User agent1 = new User("mjar@teknologisk.dk", "password123");
            agent1.setFirstName("Mateen");
            agent1.setLastName("Rafiq");
            agent1.setUsername("MJAR");
            agent1.addRole(agentRole);
            em.persist(agent1);

            User agent2 = new User("agent2@example.com", "agent123");
            agent2.setFirstName("Anne-Brit");
            agent2.setLastName("Support");
            agent2.setUsername("agent2");
            agent2.addRole(agentRole);
            em.persist(agent2);

            User agent3 = new User("freo@teknologisk.dk", "password123");
            agent3.setFirstName("Frederik");
            agent3.setLastName("Olfert");
            agent3.setUsername("FREO");
            agent3.addRole(agentRole);
            em.persist(agent3);

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

            User user3 = new User("user3@example.com", "user123");
            user3.setFirstName("Charlotte");
            user3.setLastName("Nielsen");
            user3.setUsername("user3");
            user3.addRole(userRole);
            em.persist(user3);

            User user4 = new User("user4@example.com", "user123");
            user4.setFirstName("David");
            user4.setLastName("Jensen");
            user4.setUsername("user4");
            user4.addRole(userRole);
            em.persist(user4);

            // Create Groups
            Group itRunner = new Group();
            itRunner.setName("IT-Runner");
            itRunner.setDescription("Handles technical issues");
            itRunner.getMembers().add(agent1);
            itRunner.getMembers().add(agent3);
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
            ticket1.setCreatedAt(LocalDateTime.now().minusDays(5));
            ticket1.setUpdatedAt(LocalDateTime.now().minusDays(2));
            ticket1.getTags().add(runners);
            ticket1.getTags().add(indkoeb);
            em.persist(ticket1);

            Ticket ticket2 = new Ticket();
            ticket2.setSubject("Feature request: Dark mode");
            ticket2.setDescription("Would love to have a dark mode option for the application.");
            ticket2.setRequester(user2);
            ticket2.setAssignee(agent2);
            ticket2.setGroup(itRunner);
            ticket2.setStatus(Ticket.TicketStatus.PENDING);
            ticket2.setCreatedAt(LocalDateTime.now().minusDays(3));
            ticket2.setUpdatedAt(LocalDateTime.now().minusDays(3));
            ticket2.getTags().add(support);
            em.persist(ticket2);

            Ticket ticket3 = new Ticket();
            ticket3.setSubject("Billing discrepancy");
            ticket3.setDescription("I was charged twice for my subscription this month.");
            ticket3.setRequester(user1);
            ticket3.setAssignee(agent1);
            ticket3.setGroup(supporter);
            ticket3.setStatus(Ticket.TicketStatus.SOLVED);
            ticket3.setCreatedAt(LocalDateTime.now().minusDays(7));
            ticket3.setUpdatedAt(LocalDateTime.now().minusDays(1));
            ticket3.getTags().add(runners);
            em.persist(ticket3);

            Ticket ticket4 = new Ticket();
            ticket4.setSubject("App crashes on startup");
            ticket4.setDescription("The application crashes immediately after opening.");
            ticket4.setRequester(user2);
            ticket4.setStatus(Ticket.TicketStatus.OPEN);
            ticket4.setCreatedAt(LocalDateTime.now().minusDays(1));
            ticket4.setUpdatedAt(LocalDateTime.now().minusDays(1));
            ticket4.getTags().add(runners);
            ticket4.getTags().add(indkoeb);
            em.persist(ticket4);

            // Additional 20 IT-support tickets
            Ticket ticket5 = new Ticket();
            ticket5.setSubject("VPN connection issues");
            ticket5.setDescription("Unable to connect to company VPN from home. Error code: 806");
            ticket5.setRequester(user3);
            ticket5.setAssignee(agent3);
            ticket5.setGroup(itRunner);
            ticket5.setStatus(Ticket.TicketStatus.OPEN);
            ticket5.setCreatedAt(LocalDateTime.now().minusDays(4));
            ticket5.setUpdatedAt(LocalDateTime.now());
            ticket5.getTags().add(runners);
            em.persist(ticket5);

            Ticket ticket6 = new Ticket();
            ticket6.setSubject("Printer not working");
            ticket6.setDescription("Office printer on 3rd floor shows offline status.");
            ticket6.setRequester(user4);
            ticket6.setStatus(Ticket.TicketStatus.OPEN);
            ticket6.setCreatedAt(LocalDateTime.now().minusHours(2));
            ticket6.setUpdatedAt(LocalDateTime.now().minusHours(2));
            ticket6.getTags().add(support);
            em.persist(ticket6);

            Ticket ticket7 = new Ticket();
            ticket7.setSubject("Need new laptop for employee");
            ticket7.setDescription("New hire starting next week needs a laptop configured.");
            ticket7.setRequester(user1);
            ticket7.setAssignee(agent1);
            ticket7.setGroup(itRunner);
            ticket7.setStatus(Ticket.TicketStatus.PENDING);
            ticket7.setCreatedAt(LocalDateTime.now().minusDays(6));
            ticket7.setUpdatedAt(LocalDateTime.now().minusDays(1));
            ticket7.getTags().add(indkoeb);
            em.persist(ticket7);

            Ticket ticket8 = new Ticket();
            ticket8.setSubject("Email not syncing on mobile");
            ticket8.setDescription("Company email stopped syncing on my iPhone since this morning.");
            ticket8.setRequester(user2);
            ticket8.setAssignee(agent2);
            ticket8.setGroup(supporter);
            ticket8.setStatus(Ticket.TicketStatus.OPEN);
            ticket8.setCreatedAt(LocalDateTime.now().minusHours(5));
            ticket8.setUpdatedAt(LocalDateTime.now().minusHours(1));
            ticket8.getTags().add(support);
            em.persist(ticket8);

            Ticket ticket9 = new Ticket();
            ticket9.setSubject("Software license renewal");
            ticket9.setDescription("Adobe Creative Cloud license expires next week.");
            ticket9.setRequester(user3);
            ticket9.setStatus(Ticket.TicketStatus.OPEN);
            ticket9.setCreatedAt(LocalDateTime.now().minusDays(2));
            ticket9.setUpdatedAt(LocalDateTime.now().minusDays(2));
            ticket9.getTags().add(indkoeb);
            ticket9.getTags().add(support);
            em.persist(ticket9);

            Ticket ticket10 = new Ticket();
            ticket10.setSubject("Slow computer performance");
            ticket10.setDescription("My workstation has been extremely slow for the past 3 days.");
            ticket10.setRequester(user4);
            ticket10.setAssignee(agent3);
            ticket10.setGroup(itRunner);
            ticket10.setStatus(Ticket.TicketStatus.PENDING);
            ticket10.setCreatedAt(LocalDateTime.now().minusDays(8));
            ticket10.setUpdatedAt(LocalDateTime.now().minusDays(3));
            ticket10.getTags().add(runners);
            em.persist(ticket10);

            Ticket ticket11 = new Ticket();
            ticket11.setSubject("Can't access shared drive");
            ticket11.setDescription("Getting 'Access Denied' when trying to open //server/shared");
            ticket11.setRequester(user1);
            ticket11.setAssignee(agent1);
            ticket11.setGroup(itRunner);
            ticket11.setStatus(Ticket.TicketStatus.SOLVED);
            ticket11.setCreatedAt(LocalDateTime.now().minusDays(10));
            ticket11.setUpdatedAt(LocalDateTime.now().minusDays(2));
            ticket11.getTags().add(runners);
            em.persist(ticket11);

            Ticket ticket12 = new Ticket();
            ticket12.setSubject("Password reset request");
            ticket12.setDescription("Forgot my Windows password, need urgent reset.");
            ticket12.setRequester(user2);
            ticket12.setStatus(Ticket.TicketStatus.OPEN);
            ticket12.setCreatedAt(LocalDateTime.now().minusMinutes(30));
            ticket12.setUpdatedAt(LocalDateTime.now().minusMinutes(30));
            ticket12.getTags().add(support);
            em.persist(ticket12);

            Ticket ticket13 = new Ticket();
            ticket13.setSubject("Install Microsoft Teams");
            ticket13.setDescription("Need Teams installed on my computer for remote meetings.");
            ticket13.setRequester(user3);
            ticket13.setAssignee(agent2);
            ticket13.setGroup(supporter);
            ticket13.setStatus(Ticket.TicketStatus.SOLVED);
            ticket13.setCreatedAt(LocalDateTime.now().minusDays(9));
            ticket13.setUpdatedAt(LocalDateTime.now().minusDays(4));
            ticket13.getTags().add(runners);
            ticket13.getTags().add(support);
            em.persist(ticket13);

            Ticket ticket14 = new Ticket();
            ticket14.setSubject("Keyboard malfunction");
            ticket14.setDescription("Several keys on my keyboard are not responding.");
            ticket14.setRequester(user4);
            ticket14.setStatus(Ticket.TicketStatus.OPEN);
            ticket14.setCreatedAt(LocalDateTime.now().minusHours(12));
            ticket14.setUpdatedAt(LocalDateTime.now().minusHours(12));
            ticket14.getTags().add(indkoeb);
            em.persist(ticket14);

            Ticket ticket15 = new Ticket();
            ticket15.setSubject("WiFi keeps disconnecting");
            ticket15.setDescription("Office WiFi disconnects every 15 minutes on my laptop.");
            ticket15.setRequester(user1);
            ticket15.setAssignee(agent3);
            ticket15.setGroup(itRunner);
            ticket15.setStatus(Ticket.TicketStatus.PENDING);
            ticket15.setCreatedAt(LocalDateTime.now().minusDays(5));
            ticket15.setUpdatedAt(LocalDateTime.now().minusDays(4));
            ticket15.getTags().add(runners);
            em.persist(ticket15);

            Ticket ticket16 = new Ticket();
            ticket16.setSubject("Monitor display issues");
            ticket16.setDescription("External monitor shows flickering and color distortion.");
            ticket16.setRequester(user2);
            ticket16.setAssignee(agent1);
            ticket16.setGroup(itRunner);
            ticket16.setStatus(Ticket.TicketStatus.OPEN);
            ticket16.setCreatedAt(LocalDateTime.now().minusDays(3));
            ticket16.setUpdatedAt(LocalDateTime.now());
            ticket16.getTags().add(indkoeb);
            ticket16.getTags().add(runners);
            em.persist(ticket16);

            Ticket ticket17 = new Ticket();
            ticket17.setSubject("Request for software installation");
            ticket17.setDescription("Need Python 3.11 and Visual Studio Code installed.");
            ticket17.setRequester(user3);
            ticket17.setStatus(Ticket.TicketStatus.PENDING);
            ticket17.setCreatedAt(LocalDateTime.now().minusDays(1));
            ticket17.setUpdatedAt(LocalDateTime.now().minusDays(1));
            ticket17.getTags().add(support);
            em.persist(ticket17);

            Ticket ticket18 = new Ticket();
            ticket18.setSubject("Outlook calendar sync error");
            ticket18.setDescription("Calendar events not syncing between desktop and mobile.");
            ticket18.setRequester(user4);
            ticket18.setAssignee(agent2);
            ticket18.setGroup(supporter);
            ticket18.setStatus(Ticket.TicketStatus.OPEN);
            ticket18.setCreatedAt(LocalDateTime.now().minusHours(6));
            ticket18.setUpdatedAt(LocalDateTime.now().minusHours(2));
            ticket18.getTags().add(support);
            em.persist(ticket18);

            Ticket ticket19 = new Ticket();
            ticket19.setSubject("Access to network printer");
            ticket19.setDescription("New employee needs access to color printer in accounting.");
            ticket19.setRequester(user1);
            ticket19.setStatus(Ticket.TicketStatus.OPEN);
            ticket19.setCreatedAt(LocalDateTime.now().minusHours(4));
            ticket19.setUpdatedAt(LocalDateTime.now().minusHours(4));
            ticket19.getTags().add(runners);
            em.persist(ticket19);

            Ticket ticket20 = new Ticket();
            ticket20.setSubject("Laptop battery not charging");
            ticket20.setDescription("Laptop shows 'plugged in, not charging' message.");
            ticket20.setRequester(user2);
            ticket20.setAssignee(agent3);
            ticket20.setGroup(itRunner);
            ticket20.setStatus(Ticket.TicketStatus.SOLVED);
            ticket20.setCreatedAt(LocalDateTime.now().minusDays(11));
            ticket20.setUpdatedAt(LocalDateTime.now().minusDays(5));
            ticket20.getTags().add(indkoeb);
            em.persist(ticket20);

            Ticket ticket21 = new Ticket();
            ticket21.setSubject("Software update causing crashes");
            ticket21.setDescription("After latest Windows update, Excel crashes when opening files.");
            ticket21.setRequester(user3);
            ticket21.setAssignee(agent1);
            ticket21.setGroup(itRunner);
            ticket21.setStatus(Ticket.TicketStatus.PENDING);
            ticket21.setCreatedAt(LocalDateTime.now().minusDays(6));
            ticket21.setUpdatedAt(LocalDateTime.now().minusDays(2));
            ticket21.getTags().add(support);
            ticket21.getTags().add(runners);
            em.persist(ticket21);

            Ticket ticket22 = new Ticket();
            ticket22.setSubject("Request new mouse");
            ticket22.setDescription("Current mouse broken, scroll wheel not working.");
            ticket22.setRequester(user4);
            ticket22.setStatus(Ticket.TicketStatus.OPEN);
            ticket22.setCreatedAt(LocalDateTime.now().minusHours(3));
            ticket22.setUpdatedAt(LocalDateTime.now().minusHours(3));
            ticket22.getTags().add(indkoeb);
            em.persist(ticket22);

            Ticket ticket23 = new Ticket();
            ticket23.setSubject("Cannot print to PDF");
            ticket23.setDescription("Print to PDF option missing from print dialog.");
            ticket23.setRequester(user1);
            ticket23.setAssignee(agent2);
            ticket23.setGroup(supporter);
            ticket23.setStatus(Ticket.TicketStatus.PENDING);
            ticket23.setCreatedAt(LocalDateTime.now().minusDays(7));
            ticket23.setUpdatedAt(LocalDateTime.now().minusDays(7));
            ticket23.getTags().add(support);
            em.persist(ticket23);

            Ticket ticket24 = new Ticket();
            ticket24.setSubject("Remote desktop connection failure");
            ticket24.setDescription("Unable to connect to office computer remotely. Connection times out.");
            ticket24.setRequester(user2);
            ticket24.setAssignee(agent3);
            ticket24.setGroup(itRunner);
            ticket24.setStatus(Ticket.TicketStatus.OPEN);
            ticket24.setCreatedAt(LocalDateTime.now().minusHours(1));
            ticket24.setUpdatedAt(LocalDateTime.now().minusMinutes(15));
            ticket24.getTags().add(runners);
            ticket24.getTags().add(support);
            em.persist(ticket24);

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

