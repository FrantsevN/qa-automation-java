package qa.portfolio.ui;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/** Attaches a screenshot to the Allure report when a UI test fails, before the browser is closed. */
public class ScreenshotOnFailure implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isEmpty()) {
            return;
        }
        if (context.getRequiredTestInstance() instanceof UiTestBase test
                && test.driver() instanceof TakesScreenshot camera) {
            byte[] png = camera.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("screenshot-on-failure", "image/png", new ByteArrayInputStream(png), "png");
        }
    }
}
