package qa.portfolio.ui.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutInformationPage extends BasePage {

    private static final By FIRST_NAME = testId("firstName");
    private static final By LAST_NAME = testId("lastName");
    private static final By POSTAL_CODE = testId("postalCode");
    private static final By CONTINUE = testId("continue");
    private static final By ERROR = testId("error");

    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
        waitUntilOpen("Checkout: Your Information");
    }

    public void submit(String firstName, String lastName, String postalCode) {
        Allure.step("Fill in customer details and continue", () -> {
            type(FIRST_NAME, firstName);
            type(LAST_NAME, lastName);
            type(POSTAL_CODE, postalCode);
            click(CONTINUE);
        });
    }

    public String errorMessage() {
        return text(ERROR);
    }
}
