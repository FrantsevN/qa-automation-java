package qa.portfolio.tests.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import qa.portfolio.config.Config;
import qa.portfolio.ui.UiTestBase;
import qa.portfolio.ui.pages.InventoryPage;
import qa.portfolio.ui.pages.LoginPage;

@Epic("Sauce Demo")
@Feature("Login")
class LoginTest extends UiTestBase {

    private static final String WRONG_CREDENTIALS =
            "Epic sadface: Username and password do not match any user in this service";

    @Test
    @Tag("smoke")
    @DisplayName("Standard user can log in")
    void standardUserCanLogIn() {
        new LoginPage(driver).loginAs(Config.STANDARD_USER, Config.SAUCE_PASSWORD);

        assertThat(new InventoryPage(driver).title()).isEqualTo("Products");
        assertThat(driver.getCurrentUrl()).endsWith("/inventory.html");
    }

    @Test
    @DisplayName("Locked out user cannot log in")
    void lockedOutUserSeesError() {
        LoginPage login = new LoginPage(driver);

        login.loginAs(Config.LOCKED_OUT_USER, Config.SAUCE_PASSWORD);

        assertThat(login.errorMessage()).isEqualTo("Epic sadface: Sorry, this user has been locked out.");
    }

    @ParameterizedTest(name = "{3}")
    @MethodSource("invalidCredentials")
    @DisplayName("Invalid credentials show a clear error")
    void invalidCredentialsShowError(String username, String password, String expectedError, String caseName) {
        LoginPage login = new LoginPage(driver);

        login.loginAs(username, password);

        assertThat(login.errorMessage()).isEqualTo(expectedError);
    }

    static Stream<Arguments> invalidCredentials() {
        return Stream.of(
                Arguments.of(Config.STANDARD_USER, "wrong_password", WRONG_CREDENTIALS, "wrong password"),
                Arguments.of("unknown_user", Config.SAUCE_PASSWORD, WRONG_CREDENTIALS, "unknown user"),
                Arguments.of("", Config.SAUCE_PASSWORD, "Epic sadface: Username is required", "empty username"),
                Arguments.of(Config.STANDARD_USER, "", "Epic sadface: Password is required", "empty password"));
    }

    @Test
    @DisplayName("Products page is not available without logging in")
    void productsPageRequiresLogin() {
        driver.get(Config.SAUCE_BASE_URL + "/inventory.html");

        assertThat(new LoginPage(driver).errorMessage())
                .contains("You can only access '/inventory.html' when you are logged in.");
        assertThat(driver.getCurrentUrl()).doesNotEndWith("/inventory.html");
    }
}
