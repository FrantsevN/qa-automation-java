package qa.portfolio.tests.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import qa.portfolio.config.Config;
import qa.portfolio.ui.UiTestBase;
import qa.portfolio.ui.pages.CartPage;
import qa.portfolio.ui.pages.CheckoutCompletePage;
import qa.portfolio.ui.pages.CheckoutInformationPage;
import qa.portfolio.ui.pages.CheckoutOverviewPage;
import qa.portfolio.ui.pages.InventoryPage;
import qa.portfolio.ui.pages.LoginPage;

@Epic("Sauce Demo")
@Feature("Checkout")
class CheckoutTest extends UiTestBase {

    private static final String PRODUCT = "Sauce Labs Backpack";
    private static final double PRODUCT_PRICE = 29.99;

    @BeforeEach
    void putProductInCart() {
        new LoginPage(driver).loginAs(Config.STANDARD_USER, Config.SAUCE_PASSWORD);
        InventoryPage inventory = new InventoryPage(driver);
        inventory.addToCart(PRODUCT);
        inventory.openCart();
    }

    @Test
    @Tag("smoke")
    @DisplayName("Customer can buy a product end to end")
    void customerCanCompletePurchase() {
        CartPage cart = new CartPage(driver);
        assertThat(cart.productNames()).containsExactly(PRODUCT);
        cart.checkout();

        new CheckoutInformationPage(driver).submit("Test", "Customer", "20150");

        CheckoutOverviewPage overview = new CheckoutOverviewPage(driver);
        assertThat(overview.productNames()).containsExactly(PRODUCT);
        assertThat(overview.subtotal()).isEqualTo(PRODUCT_PRICE);
        assertThat(overview.total()).isCloseTo(overview.subtotal() + overview.tax(), within(0.001));
        overview.finish();

        assertThat(new CheckoutCompletePage(driver).header()).isEqualTo("Thank you for your order!");
    }

    @ParameterizedTest(name = "{3}")
    @CsvSource(delimiter = '|', textBlock = """
            ''   | Customer | 20150 | Error: First Name is required
            Test | ''       | 20150 | Error: Last Name is required
            Test | Customer | ''    | Error: Postal Code is required
            """)
    @DisplayName("Checkout form requires every field")
    void checkoutFormRequiresEveryField(String firstName, String lastName, String postalCode, String expectedError) {
        new CartPage(driver).checkout();
        CheckoutInformationPage information = new CheckoutInformationPage(driver);

        information.submit(firstName, lastName, postalCode);

        assertThat(information.errorMessage()).isEqualTo(expectedError);
    }
}
