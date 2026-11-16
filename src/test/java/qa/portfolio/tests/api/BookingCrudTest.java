package qa.portfolio.tests.api;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qa.portfolio.api.ApiTestBase;
import qa.portfolio.api.BookingFactory;
import qa.portfolio.api.model.Booking;
import qa.portfolio.api.model.CreatedBooking;

@Epic("Restful Booker API")
@Feature("Bookings")
class BookingCrudTest extends ApiTestBase {

    @Test
    @Tag("smoke")
    @DisplayName("Create a booking")
    void createBooking() {
        Booking booking = BookingFactory.randomBooking();

        Response response = api.createBooking(booking);

        response.then().statusCode(200).body(matchesJsonSchemaInClasspath("schemas/created-booking.json"));
        CreatedBooking created = response.as(CreatedBooking.class);
        deleteAfterTest(created.bookingId());
        assertThat(created.booking()).isEqualTo(booking);
    }

    @Test
    @DisplayName("Get a booking by id")
    void getBookingById() {
        Booking booking = BookingFactory.randomBooking();
        int id = createBooking(booking);

        Response response = api.getBooking(id);

        response.then().statusCode(200).body(matchesJsonSchemaInClasspath("schemas/booking.json"));
        assertThat(response.as(Booking.class)).isEqualTo(booking);
    }

    @Test
    @DisplayName("Find a booking by guest name")
    void findBookingByName() {
        Booking booking = BookingFactory.randomBooking();
        int id = createBooking(booking);

        Response response = api.getBookingIds(Map.of("firstname", booking.firstName(), "lastname", booking.lastName()));

        response.then().statusCode(200);
        assertThat(response.jsonPath().getList("bookingid", Integer.class)).contains(id);
    }

    @Test
    @DisplayName("Replace a booking with a token")
    void updateBooking() {
        int id = createBooking(BookingFactory.randomBooking());
        Booking updated = BookingFactory.randomBooking().withAdditionalNeeds("Late checkout");

        Response response = api.updateBooking(id, updated, token);

        response.then().statusCode(200);
        assertThat(response.as(Booking.class)).isEqualTo(updated);
        assertThat(api.getBooking(id).as(Booking.class)).isEqualTo(updated);
    }

    @Test
    @DisplayName("Change some fields of a booking")
    void partialUpdateBooking() {
        Booking original = BookingFactory.randomBooking();
        int id = createBooking(original);

        Response response = api.partialUpdateBooking(id, Map.of("firstname", "Updated", "totalprice", 999), token);

        response.then().statusCode(200);
        Booking patched = response.as(Booking.class);
        assertThat(patched.firstName()).isEqualTo("Updated");
        assertThat(patched.totalPrice()).isEqualTo(999);
        assertThat(patched.lastName()).as("untouched fields keep their values").isEqualTo(original.lastName());
    }

    @Test
    @DisplayName("Delete a booking")
    void deleteBooking() {
        int id = api.createBooking(BookingFactory.randomBooking())
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        api.deleteBooking(id, token).then().statusCode(201);

        api.getBooking(id).then().statusCode(404);
    }
}
