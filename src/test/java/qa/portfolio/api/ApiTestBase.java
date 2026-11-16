package qa.portfolio.api;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import qa.portfolio.api.model.Booking;
import qa.portfolio.config.Config;

/** Gets an auth token once per test class and deletes every booking a test created. */
@Tag("api")
public abstract class ApiTestBase {

    protected static BookerClient api;
    protected static String token;

    private final List<Integer> bookingsToDelete = new ArrayList<>();

    @BeforeAll
    static void authenticate() {
        api = new BookerClient();
        token = api.createToken(Config.BOOKER_USERNAME, Config.BOOKER_PASSWORD)
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    /** Creates a booking for the current test; it is deleted when the test ends. */
    protected int createBooking(Booking booking) {
        int id = api.createBooking(booking)
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");
        deleteAfterTest(id);
        return id;
    }

    protected void deleteAfterTest(int bookingId) {
        bookingsToDelete.add(bookingId);
    }

    @AfterEach
    void deleteCreatedBookings() {
        bookingsToDelete.forEach(id -> api.deleteBooking(id, token));
        bookingsToDelete.clear();
    }
}
