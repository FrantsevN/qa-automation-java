package qa.portfolio.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import qa.portfolio.config.Config;

/** Opens a fresh browser on the Sauce Demo login page before every test and closes it afterwards. */
@Tag("ui")
@ExtendWith(ScreenshotOnFailure.class)
public abstract class UiTestBase {

    protected WebDriver driver;

    @BeforeEach
    void openBrowser() {
        driver = DriverFactory.createChrome();
        driver.get(Config.SAUCE_BASE_URL);
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    WebDriver driver() {
        return driver;
    }
}
