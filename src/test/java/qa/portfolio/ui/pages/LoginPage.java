package qa.portfolio.ui.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = testId("username");
    private static final By PASSWORD = testId("password");
    private static final By LOGIN_BUTTON = testId("login-button");
    private static final By ERROR = testId("error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void loginAs(String username, String password) {
        Allure.step("Log in as " + username, () -> {
            type(USERNAME, username);
            type(PASSWORD, password);
            click(LOGIN_BUTTON);
        });
    }

    public String errorMessage() {
        return text(ERROR);
    }
}
