package booking.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import booking.model.Booking;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    // Custom queries if needed
}