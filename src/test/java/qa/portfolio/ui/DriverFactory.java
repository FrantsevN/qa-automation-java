package qa.portfolio.ui;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import qa.portfolio.config.Config;

public final class DriverFactory {

    // Selenium ships DevTools bindings for the Chrome versions that existed when it was released and logs a
    // warning on every start with a newer Chrome. These tests use only the WebDriver protocol, so it is noise.
    // The logger lives in a field because java.util.logging drops settings of loggers nobody references.
    private static final Logger DEVTOOLS_VERSION_LOG =
            Logger.getLogger("org.openqa.selenium.devtools.CdpVersionFinder");

    static {
        DEVTOOLS_VERSION_LOG.setLevel(Level.SEVERE);
    }

    private DriverFactory() {
    }

    /** Selenium Manager downloads a chromedriver that matches the installed Chrome. */
    public static WebDriver createChrome() {
        ChromeOptions options = new ChromeOptions();
        if (Config.HEADLESS) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,900", "--disable-search-engine-choice-screen");
        // Chrome flags the public demo password in a native dialog that covers the page; switch the check off.
        options.addArguments("--disable-features=PasswordLeakDetection");
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }
}
