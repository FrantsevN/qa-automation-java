package qa.portfolio.ui.pages;

import io.qameta.allure.Allure;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutOverviewPage extends BasePage {

    private static final By ITEM_NAME = testId("inventory-item-name");
    private static final By SUBTOTAL = testId("subtotal-label");
    private static final By TAX = testId("tax-label");
    private static final By TOTAL = testId("total-label");
    private static final By FINISH = testId("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
        waitUntilOpen("Checkout: Overview");
    }

    public List<String> productNames() {
        return texts(ITEM_NAME);
    }

    public double subtotal() {
        return amount(SUBTOTAL);
    }

    public double tax() {
        return amount(TAX);
    }

    public double total() {
        return amount(TOTAL);
    }

    public void finish() {
        Allure.step("Finish the order", () -> click(FINISH));
    }

    /** "Item total: $29.99" becomes 29.99. */
    private double amount(By label) {
        String text = text(label);
        return Double.parseDouble(text.substring(text.indexOf('$') + 1));
    }
}
