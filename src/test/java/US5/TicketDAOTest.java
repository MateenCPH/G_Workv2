package US5;

import dat.config.HibernateConfig;
import dat.daos.impl.TicketDAO;
import dat.dtos.TicketDTO;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TicketDAOTest {
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static TicketDAO ticketDAO = TicketDAO.getInstance(emf);

    private static Populator populator = new Populator(emf);

    private TicketDTO openTicket, pendingTicket, solvedTicket;

    @BeforeAll
    static void beforeAll(){}

    @BeforeEach
    void setUp() {
        // TODO: Populate test database with US5.Populator when needed
        populator.populateDatabase();

        openTicket = ticketDAO.readAll().get(0);
        pendingTicket = ticketDAO.readAll().get(1);
        solvedTicket = ticketDAO.readAll().get(2);
    }

    @AfterEach
    void tearDown() {
        populator.clearDatabase();
    }

    @Test
    void testCreateTicket() {}

    @Test
    void testReadTicketById() {
        TicketDTO expected;
        expected = openTicket;
        TicketDTO actual = ticketDAO.readById(expected.getId());

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testReadAllTickets() {
        List<TicketDTO> tickets = ticketDAO.readAll();

        assertThat(tickets, hasSize(3));
        assertThat(tickets, containsInAnyOrder(openTicket, pendingTicket, solvedTicket));
    }

    @Test
    void testUpdateTicket() {}

    @Test
    void testDeleteTicket() {}

    @Test
    void testFindByStatus() {
        // TODO: Implement test for finding tickets by status
    }

    @Test
    void testFindByRequester() {
        // TODO: Implement test for finding tickets by requester
    }

    @Test
    void testFindByAssignee() {
        // TODO: Implement test for finding tickets by assignee
    }

    @Test
    void testFindByGroup() {
        // TODO: Implement test for finding tickets by group
    }

    @Test
    void testFindByTag() {
        // TODO: Implement test for finding tickets by tag
    }

    @Test
    void testSearchWithCriteria() {
        // TODO: Implement test for searching tickets with multiple criteria
    }

    @Test
    void testAssignToAgent() {
        // TODO: Implement test for assigning a ticket to an agent
    }

    @Test
    void testUnassignTicket() {
        // TODO: Implement test for unassigning a ticket
    }

    @Test
    void testUpdateStatus() {
        // TODO: Implement test for updating ticket status
    }

    @Test
    void testAddMessageToTicket() {
        // TODO: Implement test for adding a message to a ticket
    }

    @Test
    void testAddAttachmentToMessage() {
        // TODO: Implement test for adding an attachment to a message
    }

    @Test
    void testAddTagToTicket() {
        // TODO: Implement test for adding a tag to a ticket
    }

    @Test
    void testRemoveTagFromTicket() {
        // TODO: Implement test for removing a tag from a ticket
    }

    @Test
    void testFindUnassignedTickets() {
        // TODO: Implement test for finding unassigned tickets
    }

    @Test
    void testFindRecentlyUpdated() {
        // TODO: Implement test for finding recently updated tickets
    }
}
