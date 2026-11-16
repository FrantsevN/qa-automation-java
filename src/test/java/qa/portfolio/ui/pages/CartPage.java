package qa.portfolio.ui.pages;

import io.qameta.allure.Allure;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private static final By ITEM_NAME = testId("inventory-item-name");
    private static final By CHECKOUT = testId("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
        waitUntilOpen("Your Cart");
    }

    public List<String> productNames() {
        return texts(ITEM_NAME);
    }

    public void checkout() {
        Allure.step("Proceed to checkout", () -> click(CHECKOUT));
    }
}
