package US5;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dat.config.HibernateConfig;
import dat.daos.impl.TicketDAO;
import dat.dtos.TicketDTO;
import dat.exceptions.ApiException;
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
    static void beforeAll() {
    }

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
    void testCreateTicket() {
        // Arrange - Create a new ticket DTO with required fields
        TicketDTO newTicketDTO = new TicketDTO();
        newTicketDTO.setSubject("New Test Ticket");
        newTicketDTO.setDescription("This is a test ticket description");
        newTicketDTO.setStatus(dat.entities.Ticket.TicketStatus.OPEN);

        // Act - Create the ticket in a try-catch and catch exceptions thrown in DAO
        try {
            TicketDTO createdTicket = ticketDAO.create(newTicketDTO);

            // Assert - Verify the ticket was created with correct values
            assertThat(createdTicket, is(notNullValue()));
            assertThat(createdTicket.getId(), is(greaterThan(0)));
            assertThat(createdTicket.getSubject(), is(equalTo("New Test Ticket")));
            assertThat(createdTicket.getDescription(), is(equalTo("This is a test ticket description")));
            assertThat(createdTicket.getStatus(), is(equalTo(dat.entities.Ticket.TicketStatus.OPEN)));

            // Verify it's persisted by reading it back
            TicketDTO retrievedTicket = ticketDAO.readById(createdTicket.getId());
            assertThat(retrievedTicket, is(equalTo(createdTicket)));

        } catch (ApiException | InvalidFormatException | JsonParseException e) {
            e.printStackTrace();
        }
    }

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
    void testUpdateTicket() {
        // Arrange - Get an existing ticket and prepare updates
        TicketDTO ticketToUpdate = openTicket;
        int originalId = ticketToUpdate.getId();

        // Create a DTO with updated fields
        TicketDTO updateDTO = new TicketDTO();
        updateDTO.setSubject("Updated Subject");

        // Act - Update the ticket
        TicketDTO updatedTicket = ticketDAO.update(originalId, updateDTO);

        // Assert - Verify the update was successful
        assertThat(updatedTicket, is(notNullValue()));
        assertThat(updatedTicket.getId(), is(equalTo(originalId)));
        assertThat(updatedTicket.getSubject(), is(equalTo("Updated Subject")));

        // Verify the description remained unchanged (not in updateDTO)
        assertThat(updatedTicket.getDescription(), is(equalTo(ticketToUpdate.getDescription())));

        // Verify persistence by reading it back
        TicketDTO retrievedTicket = ticketDAO.readById(originalId);
        assertThat(retrievedTicket.getSubject(), is(equalTo("Updated Subject")));

        // Verify updatedAt was automatically updated (should be after createdAt)
        assertThat(retrievedTicket.getUpdatedAt(), is(notNullValue()));
        assertThat(retrievedTicket.getUpdatedAt(), is(greaterThanOrEqualTo(retrievedTicket.getCreatedAt())));
    }

    @Test
    void testDeleteTicket() {
        assertThat(ticketDAO.readAll(), hasSize(3));
        ticketDAO.delete(openTicket.getId());
        assertThat(ticketDAO.readAll(), hasSize(2));
    }

    /*
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
    }*/
}
