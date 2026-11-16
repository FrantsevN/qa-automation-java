package qa.portfolio.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private static final By HEADER = testId("complete-header");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
        waitUntilOpen("Checkout: Complete!");
    }

    public String header() {
        return text(HEADER);
    }
}
