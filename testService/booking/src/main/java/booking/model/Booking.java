package booking.model;

import io.micronaut.data.model.*;
import io.micronaut.data.annotation.*;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(GeneratedValue.Strategy.IDENTITY)
    private Long id;

    private String user;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String description;

    // getters and setters
}
