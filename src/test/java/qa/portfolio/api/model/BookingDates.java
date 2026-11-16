package qa.portfolio.api.model;

/** Dates in yyyy-MM-dd format, exactly as the API sends them. */
public record BookingDates(String checkin, String checkout) {
}
