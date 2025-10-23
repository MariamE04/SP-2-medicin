package app.controllers;

import Security.daos.SecurityDAO;
import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.entities.Medicine;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MedicineControllerTest {

   /* private Javalin app;
    private EntityManagerFactory emf;
    private SecurityDAO securityDAO;
    private String token;

    @BeforeAll
    void setup() {
        // Marker testmiljø
        System.setProperty("test.env", "true");

        // Start test-DB
        emf = HibernateConfig.getEntityManagerFactoryForTest();

        // Start server
        app = ApplicationConfig.getInstance().startServer(7007);

        // RestAssured config
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7007;
        RestAssured.basePath = "/api/medicineTracker";

        // Brug fast token
        token = "test-token";
    }


   /* @AfterAll
    void tearDown() {
        ApplicationConfig.stopServer(app);
        if (emf != null) emf.close();
    }

    @BeforeEach
    void setupTestData() {
        token = "test-token"; // bypass-token
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("DELETE FROM Medicine").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();

            // Opret en testbruger (valgfrit, kun hvis DAO kræver en user)
            var user = new SecurityDAO(emf).createUser("mariam", "test123");

            Medicine med1 = Medicine.builder()
                    .name("Panodil")
                    .type("painkiller")
                    .symptomDescription("smerter")
                    .user(user)
                    .build();

            Medicine med2 = Medicine.builder()
                    .name("Sertralin")
                    .type("antidepressant")
                    .symptomDescription("angst og depression")
                    .user(user)
                    .build();

            em.persist(med1);
            em.persist(med2);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    @Test
    void getAllMedicine() {
        given()
                .header("Authorization", "Bearer test-token")
                .when()
                .get("/medicines")
                .then()
                .statusCode(200);
    }

    @Test
    void createMedicine() {
        given()
                .header("Authorization", "Bearer test-token")
                .contentType("application/json")
                .body("{\"name\":\"Ibuprofen\",\"type\":\"painkiller\",\"symptomDescription\":\"feber\"}")
                .when()
                .post("/medicines")
                .then()
                .statusCode(201)
                .body("name", equalTo("Ibuprofen"));
    }

    @Test
    void getById() {
        // Hent første medicin for at få id
        given()
                .header("Authorization", "Bearer test-token")
                .when()
                .get("/medicines/1")
                .then()
                .statusCode(200)
                .body("name", is("Panodil"))
                .body("type", is("painkiller"))
                .body("symptomDescription", is("smerter"));
    }


    @Test
    void updateMedicine() {
        int id =
                given()
                        .header("Authorization", "Bearer test-token")
                        .when()
                        .get("/medicines")
                        .then()
                        .extract()
                        .path("[0].id");

        given()
                .header("Authorization", "Bearer test-token")
                .contentType("application/json")
                .body("{\"name\":\"Panodil Extra\",\"type\":\"painkiller\",\"symptomDescription\":\"smerter stærk\"}")
                .when()
                .put("/medicines/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Panodil Extra"));
    }

    @Test
    void deleteMedicine() {
        int id = given()
                .header("Authorization", "Bearer test-token")
                .when()
                .get("/medicines")
                .then()
                .extract()
                .path("[0].id");

        given()
                .header("Authorization", "Bearer test-token")
                .when()
                .delete("/medicines/" + id)
                .then()
                .statusCode(204);
    } */
}
