package US1;

import dat.config.HibernateConfig;
import dat.daos.impl.UserDAO;
import dat.dtos.UserDTO;
import dat.entities.User;
import dat.exceptions.ApiException;
import dat.security.daos.SecurityDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDAOTest {

    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static UserDAO userDAO = UserDAO.getInstance(emf);
    private static SecurityDAO securityDAO = new SecurityDAO(emf);

    private Populator populator = new Populator(emf);

    private static UserDTO adminDTO, agent1DTO, agent2DTO, customer1DTO, customer2DTO;

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp() {
        populator.populateDatabase();

        adminDTO = userDAO.readAll().get(0);
        agent1DTO = userDAO.readAll().get(1);
        agent2DTO = userDAO.readAll().get(2);
        customer1DTO = userDAO.readAll().get(3);
        customer2DTO = userDAO.readAll().get(4);
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE users_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Test createUser from SecurityDAO - should create a new user with USER role")
    void createUser() throws ApiException {

        assertThat(userDAO.readAll(), hasSize(5));

        User createdUser = securityDAO.createUser("test_created@user.dk", "password123");

        assertThat(userDAO.readAll(), hasSize(6));
        assertThat(userDAO.readById(6).getRoles(), containsInAnyOrder("USER"));
    }

    @Test
    @DisplayName("Test readById - should return user by ID")
    void readById() throws ApiException {
        UserDTO expected;
        expected = adminDTO;
        UserDTO actual = userDAO.readById(expected.getId());

        // Assert
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    @DisplayName("Test readAll - should return all 5 users (1 admin, 2 agents, 2 customers)")
    void readAll() {
        List<UserDTO> users = userDAO.readAll();
        assertThat(users, hasSize(5));
        assertThat(users, containsInAnyOrder(adminDTO, agent1DTO, agent2DTO, customer1DTO, customer2DTO));
    }

    @Test
    @DisplayName("Test readByEmail - should return user by email")
    void readByEmail() throws ApiException {
        UserDTO expected;
        expected = agent1DTO;
        UserDTO actual = userDAO.readByEmail(expected.getEmail());

        // Assert
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    @DisplayName("Test readByUsername - should return user by username")
    void readByUsername() throws ApiException {
        UserDTO expected;
        expected = customer2DTO;
        UserDTO actual = userDAO.readByUsername(expected.getUsername());

        // Assert
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    @DisplayName("Test readByFirstName - should return users with matching first name")
    void readByFirstName() {
        UserDTO expected;
        expected = agent2DTO;
        List<UserDTO> actual = userDAO.readByFirstName(expected.getFirstName());

        // Assert
        assertThat(actual, hasItem(expected));
    }

    @Test
    @DisplayName("Test update - should update user information")
    void updateUser() throws ApiException {
        assertThat(userDAO.readById(customer1DTO.getId()).getFirstName(), is(equalTo("Alice")));
        customer1DTO.setFirstName("UpdatedName");

        try {
            userDAO.update(customer1DTO.getId(), customer1DTO);
            assertThat(userDAO.readById(customer1DTO.getId()).getFirstName(), is(equalTo("UpdatedName")));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test delete - should delete user by ID")
    void deleteUser() throws ApiException {
        assertThat(userDAO.readAll(), hasSize(5));
        userDAO.delete(agent2DTO.getId());
        assertThat(userDAO.readAll(), hasSize(4));
    }

    /*
    @Test
    @DisplayName("Test readAll - should return only active users")
    void readAllActiveUsers() {

    }*/
}
