package GroupTest;

import dat.config.HibernateConfig;
import dat.daos.impl.GroupDAO;
import dat.dtos.GroupDTO;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupDAOTest {

    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static GroupDAO groupDAO = GroupDAO.getInstance(emf);

    private Populator populator = new Populator(emf);

    private static GroupDTO supportTeamDTO, developmentTeamDTO, salesTeamDTO;

    @BeforeEach
    void setUp() {
        populator.populateDatabase();

        List<GroupDTO> allGroups = groupDAO.readAll();
        supportTeamDTO = allGroups.stream()
                .filter(g -> "Support Team".equals(g.getName()))
                .findFirst()
                .orElseThrow();
        developmentTeamDTO = allGroups.stream()
                .filter(g -> "Development Team".equals(g.getName()))
                .findFirst()
                .orElseThrow();
        salesTeamDTO = allGroups.stream()
                .filter(g -> "Sales Team".equals(g.getName()))
                .findFirst()
                .orElseThrow();
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Group").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE groups_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Test readById - should return group by ID")
    void readById() throws ApiException {
        // Arrange
        GroupDTO expected = supportTeamDTO;

        // Act
        GroupDTO actual = groupDAO.readById(expected.getId());

        // Assert
        assertThat(actual.getId(), is(equalTo(expected.getId())));
        assertThat(actual.getName(), is(equalTo(expected.getName())));
        assertThat(actual.getDescription(), is(equalTo(expected.getDescription())));
    }

    @Test
    @DisplayName("Test readById - should throw ApiException when group not found")
    void readByIdNotFound() {
        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            groupDAO.readById(999);
        });
        
        assertThat(exception.getStatusCode(), is(404));
        assertThat(exception.getMessage(), is(equalTo("Group not found")));
    }

    @Test
    @DisplayName("Test readAll - should return all 3 groups")
    void readAll() {
        // Act
        List<GroupDTO> groups = groupDAO.readAll();

        // Assert
        assertThat(groups, hasSize(3));
        assertThat(groups, containsInAnyOrder(supportTeamDTO, developmentTeamDTO, salesTeamDTO));
    }

    @Test
    @DisplayName("Test readAll - should return empty list when no groups exist")
    void readAllEmpty() {
        // Arrange - clean database
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Group").executeUpdate();
            em.getTransaction().commit();
        }

        // Act
        List<GroupDTO> groups = groupDAO.readAll();

        // Assert
        assertThat(groups, is(empty()));
    }
}
