package qa.portfolio.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatedBooking(@JsonProperty("bookingid") int bookingId, Booking booking) {
}
