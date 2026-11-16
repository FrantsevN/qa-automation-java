package qa.portfolio.api;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import qa.portfolio.api.model.Booking;
import qa.portfolio.api.model.BookingDates;

public final class BookingFactory {

    private BookingFactory() {
    }

    /** A valid booking with a unique guest name, so tests never collide on the shared public API. */
    public static Booking randomBooking() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        LocalDate checkin = LocalDate.now().plusDays(random.nextInt(10, 61));
        LocalDate checkout = checkin.plusDays(random.nextInt(1, 15));
        return new Booking(
                "Qa" + UUID.randomUUID().toString().substring(0, 8),
                "Automation",
                random.nextInt(50, 501),
                true,
                new BookingDates(checkin.toString(), checkout.toString()),
                "Breakfast");
    }
}
