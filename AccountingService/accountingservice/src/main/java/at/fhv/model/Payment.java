package at.fhv.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Serdeable
@Entity
@Table(name = "payment_records")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String paymentId;
    private String termId;
    private String userId;
    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDate paymentDate;
    private LocalDate paymentDeadline;


    public Payment() {
        // Standard-Constructor for JPA
    }

    public Payment(String termId, String userId, double amount, LocalDate paymentDate, LocalDate paymentDeadline, boolean paid) {
        this.termId = termId;
        this.userId = userId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentDeadline = paymentDeadline;
        this.paymentId = termId  + userId;
        this.status = paid ? PaymentStatus.PAID : PaymentStatus.PENDING;
    }


    // getters
    public String getPaymentId() {
      return paymentId;
    }

    public String getUserId() {
      return userId;
    }

    public String getTermId() {
      return termId;
    }

    // setters
    public void setPaymentId(String paymentId) {
      this.paymentId = paymentId;
    }

    public void setUserId(String userId) {
      this.userId = userId;
    }

    public void setTermId(String termId) {
      this.termId = termId;
    }

    // equals and hashCode based on paymentId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paymentId, payment.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }

    // toString
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", termId='" + termId + '\'' +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                ", paymentDate=" + paymentDate +
                ", paymentDeadline=" + paymentDeadline +
                '}';
    }
}
