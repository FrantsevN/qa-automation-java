package qa.portfolio.tests.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qa.portfolio.config.Config;
import qa.portfolio.ui.UiTestBase;
import qa.portfolio.ui.pages.InventoryPage;
import qa.portfolio.ui.pages.LoginPage;

@Epic("Sauce Demo")
@Feature("Products")
class InventoryTest extends UiTestBase {

    private InventoryPage inventory;

    @BeforeEach
    void logIn() {
        new LoginPage(driver).loginAs(Config.STANDARD_USER, Config.SAUCE_PASSWORD);
        inventory = new InventoryPage(driver);
    }

    @Test
    @DisplayName("Catalog shows all six products")
    void catalogShowsSixProducts() {
        assertThat(inventory.productNames()).hasSize(6);
    }

    @Test
    @DisplayName("Products can be sorted by name from Z to A")
    void sortByNameDescending() {
        inventory.sortBy("za");

        assertThat(inventory.productNames()).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    @DisplayName("Products can be sorted by price from low to high")
    void sortByPriceAscending() {
        inventory.sortBy("lohi");

        assertThat(inventory.productPrices()).isSorted();
    }

    @Test
    @DisplayName("Products can be sorted by price from high to low")
    void sortByPriceDescending() {
        inventory.sortBy("hilo");

        assertThat(inventory.productPrices()).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    @DisplayName("Cart badge follows adding and removing products")
    void cartBadgeFollowsCartChanges() {
        inventory.addToCart("Sauce Labs Backpack");
        inventory.addToCart("Sauce Labs Bike Light");
        assertThat(inventory.cartCount()).isEqualTo(2);

        inventory.removeFromCart("Sauce Labs Backpack");
        assertThat(inventory.cartCount()).isEqualTo(1);

        inventory.removeFromCart("Sauce Labs Bike Light");
        assertThat(inventory.cartCount()).isZero();
    }
}
