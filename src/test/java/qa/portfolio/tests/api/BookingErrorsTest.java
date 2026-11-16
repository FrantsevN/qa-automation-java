package qa.portfolio.tests.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qa.portfolio.api.ApiTestBase;
import qa.portfolio.api.BookingFactory;

@Epic("Restful Booker API")
@Feature("Bookings: errors and access control")
class BookingErrorsTest extends ApiTestBase {

    private static final ObjectMapper JSON = new ObjectMapper();

    @Test
    @DisplayName("Update without a token is forbidden")
    void updateWithoutTokenIsForbidden() {
        int id = createBooking(BookingFactory.randomBooking());

        api.updateBooking(id, BookingFactory.randomBooking(), null).then().statusCode(403);
    }

    @Test
    @DisplayName("Delete without a token is forbidden")
    void deleteWithoutTokenIsForbidden() {
        int id = createBooking(BookingFactory.randomBooking());

        api.deleteBooking(id, null).then().statusCode(403);

        api.getBooking(id).then().statusCode(200);
    }

    @Test
    @DisplayName("Unknown booking id returns 404")
    void unknownBookingReturns404() {
        api.getBooking(999_999_999).then().statusCode(404);
    }

    @Test
    @DisplayName("Update with missing required fields is rejected")
    void updateWithMissingFieldsReturns400() {
        int id = createBooking(BookingFactory.randomBooking());

        api.updateBooking(id, bookingWithout("lastname"), token).then().statusCode(400);
    }

    @Test
    @Disabled("Known defect: the API answers 500 Internal Server Error to a validation problem")
    @DisplayName("Create with missing required fields returns 400")
    void createWithMissingFieldsReturns400() {
        api.createBooking(bookingWithout("firstname")).then().statusCode(400);
    }

    private static Map<String, Object> bookingWithout(String field) {
        Map<String, Object> payload = JSON.convertValue(BookingFactory.randomBooking(), new TypeReference<>() {
        });
        payload.remove(field);
        return payload;
    }
}
