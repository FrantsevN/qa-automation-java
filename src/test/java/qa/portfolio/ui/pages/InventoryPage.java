package qa.portfolio.ui.pages;

import io.qameta.allure.Allure;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class InventoryPage extends BasePage {

    private static final By TITLE = testId("title");
    private static final By ITEM_NAME = testId("inventory-item-name");
    private static final By ITEM_PRICE = testId("inventory-item-price");
    private static final By ITEM_IMAGE = By.cssSelector("img.inventory_item_img");
    private static final By SORT_SELECT = testId("product-sort-container");
    private static final By ACTIVE_SORT = testId("active-option");
    private static final By CART_BADGE = testId("shopping-cart-badge");
    private static final By CART_LINK = testId("shopping-cart-link");

    public InventoryPage(WebDriver driver) {
        super(driver);
        waitUntilOpen("Products");
    }

    public String title() {
        return text(TITLE);
    }

    public List<String> productNames() {
        return texts(ITEM_NAME);
    }

    public List<Double> productPrices() {
        return texts(ITEM_PRICE).stream()
                .map(price -> Double.parseDouble(price.replace("$", "")))
                .toList();
    }

    public List<String> imageSources() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ITEM_IMAGE));
        return driver.findElements(ITEM_IMAGE).stream()
                .map(image -> image.getDomAttribute("src"))
                .toList();
    }

    /** option is the value of the sort select: az, za, lohi or hilo. */
    public void sortBy(String option) {
        Allure.step("Sort products by " + option, () -> {
            WebElement select = visible(SORT_SELECT);
            String label = select.findElement(By.cssSelector("option[value='" + option + "']"))
                    .getDomProperty("textContent");
            new Select(select).selectByValue(option);
            // Sorting re-renders the header together with the list, and React creates the select anew,
            // so the old element is not touched again. The label next to it changes in that same render.
            waitForText(ACTIVE_SORT, label);
        });
    }

    public void addToCart(String productName) {
        Allure.step("Add " + productName + " to the cart", () -> {
            click(testId("add-to-cart-" + slug(productName)));
            visible(testId("remove-" + slug(productName)));
        });
    }

    public void removeFromCart(String productName) {
        Allure.step("Remove " + productName + " from the cart", () -> {
            click(testId("remove-" + slug(productName)));
            visible(testId("add-to-cart-" + slug(productName)));
        });
    }

    /** Number on the cart icon; 0 when the badge is hidden. */
    public int cartCount() {
        List<WebElement> badge = driver.findElements(CART_BADGE);
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText());
    }

    public void openCart() {
        Allure.step("Open the cart", () -> click(CART_LINK));
    }
}
