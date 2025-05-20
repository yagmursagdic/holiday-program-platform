package booking.controller;

import java.util.Optional;

import booking.model.Booking;
import booking.service.BookingService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

@Controller("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
      this.bookingService = bookingService;
    }
    
    @Get("/")
    public Iterable<Booking> list() {
        return bookingService.listAll();
    }

    @Get("/{id}")
    public Optional<Booking> getBooking(Long id) {
      return bookingService.findById(id);
    }
    
    @Get("/ping")
    public String getData() {
      return "pong";
    }

    @Post("/")
    // saves changes, but also creates new booking if it doesn't exist 
    public Booking save(@Body Booking booking) {
      return bookingService.save(booking);
    }
    
    @Delete("/{id}")
    public void delete(Long id) {
      bookingService.delete(id);
    }
    
    /*
    🧠 Step 6: Add Advanced Querying
      For CQRS, you can separate read and write operations via different services/controllers. 
      For API Composition, you may want to include endpoints that aggregate data from other services (use RestClient or Feign).
     */
    @Get("/bookings-summary")
    public BookingSummary getSummary() {
      // Combine data from this and other microservices
    }

}
