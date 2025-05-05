package at.fhv.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.time.LocalDate;

@Serdeable
@Entity
@Table(name = "payment_records")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentId;
    private Long termId;
    private Long userId;
    private double amount;
    private PaymentStatus status;
    private LocalDate paymentDate;
    private LocalDate paymentDeadline;


    public Payment() {
        // Standard-Konstruktor für JPA
    }

    public Payment(Long termId, Long userId, double amount, LocalDate paymentDate, LocalDate paymentDeadline, boolean paid) {
        this.termId = termId;
        this.userId = userId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentDeadline = paymentDeadline;
        this.paymentId = termId + "-" + userId;
        this.status = paid ? PaymentStatus.PAID : PaymentStatus.OPEN;
    }


    // getters
    public Long getId() {
      return id;
    }

    public Long getUserId() {
      return userId;
    }

    public Long getTermId() {
      return termId;
    }

    // setters
    public void setId(Long id) {
      this.id = id;
    }

    public void setUserId(Long userId) {
      this.userId = userId;
    }

    public void setTermId(Long termId) {
      this.termId = termId;
    }
}
