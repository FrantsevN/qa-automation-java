package qa.portfolio.tests.api;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import qa.portfolio.api.ApiTestBase;
import qa.portfolio.config.Config;

@Epic("Restful Booker API")
@Feature("Auth")
class AuthTest extends ApiTestBase {

    @Test
    @Tag("smoke")
    @DisplayName("API is up")
    void healthCheck() {
        api.ping().then().statusCode(201);
    }

    @Test
    @DisplayName("Valid credentials return a token")
    void validCredentialsReturnToken() {
        api.createToken(Config.BOOKER_USERNAME, Config.BOOKER_PASSWORD)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/token.json"));
    }

    @ParameterizedTest(name = "{2}")
    @CsvSource(delimiter = '|', textBlock = """
            admin   | wrong-password | wrong password
            unknown | password123    | unknown user
            ''      | ''             | empty credentials
            """)
    @DisplayName("Invalid credentials do not return a token")
    void invalidCredentialsAreRejected(String username, String password, String caseName) {
        // The API reports failed logins in the body and still answers 200.
        api.createToken(username, password)
                .then()
                .statusCode(200)
                .body("reason", equalTo("Bad credentials"))
                .body("token", nullValue());
    }
}
