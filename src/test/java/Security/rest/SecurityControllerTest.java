package Security.rest;

import app.config.ApplicationConfig;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class SecurityControllerTest {
    private static Javalin app;

    @BeforeAll
    static void setup() {
        System.setProperty("test.env", "true");
        app = ApplicationConfig.getInstance().startServer(7007);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7007;
        RestAssured.basePath = "/api/medicineTracker";
    }

    @AfterAll
    static void tearDown() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void registerUser() {
        given()
                .contentType("application/json")
                .body("{\"username\":\"mariamTest1\",\"password\":\"test123\"}")
                .when()
                .post("auth/register")
                .then()
                .statusCode(201)
                .body("username", is("mariamTest1"))
                .body("token", notNullValue());
    }

    @Test
    void loginUser() {
        // Registrer først brugeren
        given()
                .contentType("application/json")
                .body("{\"username\":\"mariamTest\",\"password\":\"test123\"}")
                .when()
                .post("auth/register")
                .then()
                .statusCode(201);

        // Forsøger derefter at logge ind
        given()
                .contentType("application/json")
                .body("{\"username\":\"mariamTest\",\"password\":\"test123\"}")
                .when()
                .post("auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("username", is("mariamTest"));
    }

}