package booking.service;

import booking.repository.BookingRepository;
import booking.model.Booking;

import jakarta.persistence.*;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
      this.bookingRepository = bookingRepository;
    }
    
    public Iterable<Booking> listAll() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking save(Booking booking) {
      return bookingRepository.save(booking);
    }
    
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }

    // More methods like findById, etc.
}
