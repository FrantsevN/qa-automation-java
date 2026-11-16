package qa.portfolio.config;

/** Test settings. Each value can be overridden with -Dproperty=value or an environment variable. */
public final class Config {

    public static final String SAUCE_BASE_URL =
            value("sauce.baseUrl", "SAUCE_BASE_URL", "https://www.saucedemo.com");
    public static final String SAUCE_PASSWORD = value("sauce.password", "SAUCE_PASSWORD", "secret_sauce");
    public static final String STANDARD_USER = "standard_user";
    public static final String LOCKED_OUT_USER = "locked_out_user";
    public static final String PROBLEM_USER = "problem_user";

    public static final String BOOKER_BASE_URL =
            value("booker.baseUrl", "BOOKER_BASE_URL", "https://restful-booker.herokuapp.com");
    public static final String BOOKER_USERNAME = value("booker.username", "BOOKER_USERNAME", "admin");
    public static final String BOOKER_PASSWORD = value("booker.password", "BOOKER_PASSWORD", "password123");

    /** Headless in CI (GitHub Actions sets CI=true), visible browser on a laptop. Force it with -Dheadless=true. */
    public static final boolean HEADLESS = Boolean.parseBoolean(value("headless", "CI", "false"));

    private Config() {
    }

    private static String value(String property, String environmentVariable, String defaultValue) {
        String value = System.getProperty(property);
        if (value == null || value.isBlank()) {
            value = System.getenv(environmentVariable);
        }
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
