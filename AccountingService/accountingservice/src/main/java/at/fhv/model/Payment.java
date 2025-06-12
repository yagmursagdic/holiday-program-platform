package at.fhv.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

@Serdeable
@Entity
@Table(name = "payment_records")
public class Payment {

    @Id
    @Column(name = "payment_id")
    private String paymentId;

    @PrePersist
    public void generateId() {
        if (paymentId == null) {
            paymentId = UUID.randomUUID().toString();
        }
    }

    private String userId;
    private String termId;
    private double amount;

    @Enumerated(EnumType.STRING)
    private LocalDate paymentDate;
    private LocalDate paymentDeadline;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


    public Payment() {
        // Standard-Constructor for JPA
    }

    public Payment(String userId, String termId, double amount, LocalDate paymentDate, LocalDate paymentDeadline, PaymentStatus status) {
        this.userId = userId;
        this.termId = termId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentDeadline = paymentDeadline;
        this.status = status;
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

    public double getAmount() {
      return amount;
    }

    public LocalDate getPaymentDate() {
      return paymentDate;
    }
    
    public LocalDate getPaymentDeadline() {
      return paymentDeadline;
    }

    public PaymentStatus getStatus() {
      return status;
    }

    // setters
    public void setUserId(String userId) {
      this.userId = userId;
    }
    
    public void setTermId(String termId) {
      this.termId = termId;
    }

    public void setAmount(double amount) {
      this.amount = amount;
    }

    public void setPaymentDate(LocalDate paymentDate) {
      this.paymentDate = paymentDate;
    }
    
    public void setPaymentDeadline(LocalDate paymentDeadline) {
      this.paymentDeadline = paymentDeadline;
    }

    public void setStatus(PaymentStatus status) {
      this.status = status;
    }


    // equals and hashCode based on paymentId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment payment)) return false;
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
