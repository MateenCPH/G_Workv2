package US1;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.impl.UserDAO;
import dat.dtos.UserDTO;
import dat.entities.User;
import dat.security.daos.SecurityDAO;
import dat.security.tokenSecurity.TokenSecurity;
import dat.utils.Utils;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoginSteps {

    private static Javalin app;
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final SecurityDAO securityDAO = new SecurityDAO(emf);

    private static final String BASE_URL = "http://localhost:5005/api"; // match your app

    private static User testUser;

    @BeforeAll
    static void beforeAll() {
        app = ApplicationConfig.startServer(5005); // same as in prod, but using test EMF internally
    }

    @BeforeEach
    void setUp() {
        // Clean DB and create a known user for login
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE users_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }

        // Create a user with USER role via SecurityDAO
        testUser = securityDAO.createUser("agent1@user.dk", "password123");
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

    @AfterAll
    static void afterAll() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    @DisplayName("Login with valid credentials returns 200 and JWT token with 'user' role")
    void loginWithValidCredentials() throws Exception {
        // Call POST /auth/login with JSON body
        var response =
                given()
                        .contentType("application/json")
                        .body("""
                                {
                                    "email": "agent1@user.dk",
                                    "password": "password123"
                                }
                                """)
                        .when()
                        .post(BASE_URL + "/auth/login")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .jsonPath();
        String token = response.getString("token");
        String emailFromResponse = response.getString("email");

        assertThat(token, not(is(emptyOrNullString())));
        assertThat(emailFromResponse, is("agent1@user.dk"));

        TokenSecurity tokenSecurity = new TokenSecurity();
        String secret = Utils.getPropertyValue("SECRET_KEY", "config.properties");

        var userFromToken = tokenSecurity.getUserWithRolesFromToken(token);
        assertThat(userFromToken.getRoles(), hasItem("user"));
    }

    @Test
    @DisplayName("Login with wrong password return 401")
    void loginWithWrongPassword() {
        given()
                .contentType("application/json")
                .body("""
                        {
                        "email": "agent1@user.dk",
                        "password": "wrongpassword"
                        }
                        """)
                .when()
                .post(BASE_URL + "/auth/login")
                .then()
                .log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("Login with non-existing email")
    void loginWithNonExistingEmail() {
        given()
                .contentType("application/json")
                .body("""
                        {
                        "email": "nonexistent@user.dk",
                        "password": "password123"
                        }
                        """)
                .when()
                .post(BASE_URL + "/auth/login")
                .then()
                .log().all()
                .statusCode(401);
    }
}
