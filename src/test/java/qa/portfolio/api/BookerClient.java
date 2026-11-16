package qa.portfolio.api;

import static io.restassured.RestAssured.given;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import qa.portfolio.config.Config;

/** Thin wrapper around the Restful Booker API: one method per endpoint. */
public class BookerClient {

    private final RequestSpecification spec;

    public BookerClient() {
        spec = new RequestSpecBuilder()
                .setBaseUri(Config.BOOKER_BASE_URL)
                .setContentType(ContentType.JSON)
                // The API picks the response format from the Accept header and answers 418 to unknown values,
                // so it must be exactly application/json.
                .setAccept("application/json")
                .addFilter(new AllureRestAssured())
                .build();
    }

    public Response ping() {
        return given(spec).get("/ping");
    }

    public Response createToken(String username, String password) {
        return given(spec).body(Map.of("username", username, "password", password)).post("/auth");
    }

    public Response getBookingIds(Map<String, String> filters) {
        return given(spec).queryParams(filters).get("/booking");
    }

    public Response getBooking(int id) {
        return given(spec).get("/booking/{id}", id);
    }

    public Response createBooking(Object booking) {
        return given(spec).body(booking).post("/booking");
    }

    public Response updateBooking(int id, Object booking, String token) {
        return withToken(token).body(booking).put("/booking/{id}", id);
    }

    public Response partialUpdateBooking(int id, Map<String, ?> fields, String token) {
        return withToken(token).body(fields).patch("/booking/{id}", id);
    }

    public Response deleteBooking(int id, String token) {
        return withToken(token).delete("/booking/{id}", id);
    }

    private RequestSpecification withToken(String token) {
        RequestSpecification request = given(spec);
        return token == null ? request : request.cookie("token", token);
    }
}
