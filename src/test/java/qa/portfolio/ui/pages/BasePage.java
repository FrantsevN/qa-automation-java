package qa.portfolio.ui.pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Shared helpers for all Sauce Demo pages. Every interaction waits explicitly instead of sleeping.
 *
 * <p>The shop is a React app. It switches pages a moment after the click that causes it, and it creates
 * some elements anew every time a page re-renders. So the helpers never keep an element between steps:
 * they find it by locator on every attempt and try again if it was replaced in the middle of a read.
 */
public abstract class BasePage {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final Duration POLLING = Duration.ofMillis(100);
    private static final By PAGE_TITLE = testId("title");

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT, POLLING);
        this.wait.ignoring(StaleElementReferenceException.class);
    }

    /** Sauce Demo marks elements with data-test attributes, which survive layout changes. */
    protected static By testId(String id) {
        return By.cssSelector("[data-test='" + id + "']");
    }

    /** 'Sauce Labs Backpack' becomes 'sauce-labs-backpack', the format used in button test ids. */
    protected static String slug(String productName) {
        return productName.trim().toLowerCase().replaceAll("\\s+", "-");
    }

    /**
     * Waits until the page with this header is on screen. Page objects call it first: the previous page
     * stays in the DOM for a moment after a click, and some of its locators match the next page too.
     */
    protected final void waitUntilOpen(String pageTitle) {
        waitForText(PAGE_TITLE, pageTitle);
    }

    /** Waits for the exact text of an element. Works for elements that CSS hides on narrow screens too. */
    protected final void waitForText(By locator, String expected) {
        wait.until(textToBe(locator, expected));
    }

    private static ExpectedCondition<Boolean> textToBe(By locator, String expected) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver browser) {
                return expected.equals(browser.findElement(locator).getDomProperty("textContent"));
            }

            /** Shown in the timeout message, so a failed wait says what it was waiting for. */
            @Override
            public String toString() {
                return "text of " + locator + " to be '" + expected + "'";
            }
        };
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        WebElement field = visible(locator);
        if (!text.isEmpty()) {
            field.sendKeys(text);
        }
    }

    /** Text of a visible element. It is read inside the wait, so a re-render in the middle is retried. */
    protected String text(By locator) {
        return wait.until(browser -> {
            WebElement element = browser.findElement(locator);
            return element.isDisplayed() ? element.getText() : null;
        });
    }

    /** Texts of all elements matching the locator, once every one of them is visible. */
    protected List<String> texts(By locator) {
        return wait.until(browser -> {
            List<WebElement> elements = browser.findElements(locator);
            boolean allVisible = !elements.isEmpty() && elements.stream().allMatch(WebElement::isDisplayed);
            return allVisible ? elements.stream().map(WebElement::getText).toList() : null;
        });
    }
}
