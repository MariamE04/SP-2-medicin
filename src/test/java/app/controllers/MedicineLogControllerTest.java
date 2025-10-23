package app.controllers;

import Security.daos.SecurityDAO;
import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.entities.Medicine;
import app.entities.MedicineLog;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MedicineLogControllerTest {
  /*  private Javalin app;
    private EntityManagerFactory emf;
    private String token;

    @BeforeAll
    void setup() {
        System.setProperty("test.env", "true");
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = ApplicationConfig.getInstance().startServer(7007);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7007;
        RestAssured.basePath = "/api/medicineTracker";

        token = "test-token";
    }

    @AfterAll
    void tearDown() {
        ApplicationConfig.stopServer(app);
        if (emf != null) emf.close();
    }

    @BeforeEach
    void setupTestData() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("DELETE FROM MedicineLog").executeUpdate();
            em.createQuery("DELETE FROM Medicine").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();

            // Opret testbruger og medicin
            var securityDAO = new SecurityDAO(emf);
            var user = securityDAO.createUser("mariam", "test123");

            Medicine med = Medicine.builder()
                    .name("Panodil")
                    .type("painkiller")
                    .symptomDescription("smerter")
                    .user(user)
                    .build();
            em.persist(med);

            MedicineLog log1 = MedicineLog.builder()
                    .dose(25.0)
                    .takenAt(LocalDateTime.now())
                    .user(user)
                    .medicine(med)
                    .build();

            MedicineLog log2 = MedicineLog.builder()
                    .dose(20.0)
                    .takenAt(LocalDateTime.now())
                    .user(user)
                    .medicine(med)
                    .build();

            em.persist(log1);
            em.persist(log2);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Test
    void getAllLogs() {
        given()
                .header("Authorization", "Bearer test-token")
                .when()
                .get("/medicineLog")
                .then()
                .statusCode(200);
    }

    @Test
    void getLogsByMedicine() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/medicineLog/1")
                .then()
                .statusCode(200)
                .body("dose", is(25.0F))
                .body("takenAt", notNullValue());

    }

    @Test
    void createLog() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("{\"medicineName\":\"Panodil\",\"dose\":3,\"takenAt\":\"2025-10-22T15:30:00\"}")
                .when()
                .post("/medicineLog")
                .then()
                .statusCode(201)
                .body("medicineName", equalTo("Panodil"))
                .body("dose", equalTo(3.0F));
    }

    @Test
    void updateLog() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("{\"medicineName\":\"Panodil\",\"dose\":1,\"takenAt\":\"2025-10-22T15:30:00\"}")
                .when()
                .post("/medicineLog")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

    }

    @Test
    void deleteLog() {
        int id = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("{\"medicineName\":\"Panodil\",\"dose\":2,\"takenAt\":\"2025-10-22T12:00:00\"}")
                .when()
                .post("/medicineLog")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/medicineLog/" + id)
                .then()
                .statusCode(204);

        // Bekræft at GET nu returnerer 404
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/logs/" + id)
                .then()
                .statusCode(404);
    } */
}
