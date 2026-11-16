# QA automation in Java: Selenium + REST Assured + JUnit 5

[![tests](https://github.com/FrantsevN/qa-automation-java/actions/workflows/tests.yml/badge.svg)](https://github.com/FrantsevN/qa-automation-java/actions/workflows/tests.yml)

UI and API test suites for two public practice applications, built the way I set up automation for client projects: page objects, explicit waits, isolated test data, readable reports and a CI pipeline that runs on every push.

**Allure report:** https://frantsevn.github.io/qa-automation-java/

| Suite | Application | Stack |
| --- | --- | --- |
| UI | [Sauce Demo](https://www.saucedemo.com), an online shop | Selenium WebDriver, JUnit 5, AssertJ, Page Object pattern |
| API | [Restful Booker](https://restful-booker.herokuapp.com), a hotel booking API | REST Assured, Jackson, JSON Schema |

## What is covered

**UI (Sauce Demo)**
- Login: valid user, locked user, wrong and empty credentials, access to protected pages
- Products: catalog, sorting by name and price, cart badge
- Checkout: full purchase from cart to order confirmation, totals check, form validation

**API (Restful Booker)**
- Auth: token for valid credentials, rejected logins, health check
- Bookings: create, read, search by name, full and partial update, delete
- Access control and errors: requests without a token, unknown ids, invalid payloads
- Responses are checked against JSON schemas and mapped to Java records

## Bugs found

Tests for known defects are kept and marked `@Disabled` with the reason, so anyone can enable one and reproduce the bug.

| Application | Defect |
| --- | --- |
| Sauce Demo | `problem_user` sees the same placeholder image for every product |
| Restful Booker | Creating a booking without required fields returns `500 Internal Server Error` instead of `400 Bad Request` |

## Run locally

Requires JDK 21, Maven and Google Chrome. Selenium downloads a matching chromedriver by itself.

```bash
mvn test                      # everything, Chrome opens visibly
mvn test -Dgroups=api         # API tests only
mvn test -Dgroups=ui          # UI tests only
mvn test -Dgroups=smoke       # critical path only
mvn test -Dheadless=true      # UI tests without a visible browser
```

Open the Allure report (needs the [Allure CLI](https://allurereport.org/docs/install/)):

```bash
allure serve target/allure-results
```

## Project structure

```
src/test/java/qa/portfolio/
  api/          REST Assured client, Java records for the API, test data factory, base class
  config/       settings, overridable with -D flags or environment variables
  ui/           WebDriver setup, screenshot on failure, page objects
  tests/api/    API tests
  tests/ui/     UI tests
src/test/resources/schemas/   JSON schemas for API responses
.github/workflows/tests.yml   CI: tests, Allure report, GitHub Pages
```

## Design notes

- **Stable locators.** Page objects use the `data-test` attributes of the app, not CSS classes or text.
- **No sleeps.** Every interaction waits for a concrete condition, for example the cart button switching to "Remove" after a product is added.
- **Built for a React front end.** The shop switches pages a moment after a click and re-creates some elements when a page re-renders. Each page object first waits for its own header, and every read is retried if an element is replaced halfway, so the suite does not flake with `StaleElementReferenceException`.
- **No shared state.** Every API test creates its own booking with a unique name and deletes it afterwards, so tests can run in any order on a public API that other people use at the same time.
- **Useful failures.** Failed UI tests attach a screenshot to the Allure report; every API call a test makes is logged into the report with request and response.
- **CI.** GitHub Actions runs the suites on every push and pull request, then publishes the Allure report to GitHub Pages.
