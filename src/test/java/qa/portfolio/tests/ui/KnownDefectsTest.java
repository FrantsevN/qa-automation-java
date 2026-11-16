package qa.portfolio.tests.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qa.portfolio.config.Config;
import qa.portfolio.ui.UiTestBase;
import qa.portfolio.ui.pages.InventoryPage;
import qa.portfolio.ui.pages.LoginPage;

/** Checks for defects found in the demo shop. Enable a test to reproduce its bug. */
@Epic("Sauce Demo")
@Feature("Known defects")
class KnownDefectsTest extends UiTestBase {

    @Test
    @Disabled("Known defect: problem_user sees the same placeholder image for every product")
    @DisplayName("problem_user sees a different image for every product")
    void problemUserSeesUniqueProductImages() {
        new LoginPage(driver).loginAs(Config.PROBLEM_USER, Config.SAUCE_PASSWORD);

        assertThat(new InventoryPage(driver).imageSources()).doesNotHaveDuplicates();
    }
}
