package at.fhv.model;

import java.time.LocalDate;
import java.util.Objects;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Serdeable
@Entity
@Table(name = "payment_records")
public class Payment {

    //UUID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;
    private String termId;
    private String paymentId;
    private double amount;

    @Enumerated(EnumType.STRING)
    private LocalDate paymentDate;
    private LocalDate paymentDeadline;
    private PaymentStatus status;


    public Payment() {
        // Standard-Constructor for JPA
    }

    public Payment(String termId, String userId, double amount, LocalDate paymentDate, LocalDate paymentDeadline, boolean paid) {
        this.userId = userId;
        this.termId = termId;
        this.paymentId = termId  + userId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentDeadline = paymentDeadline;
        this.status = (paid ? PaymentStatus.PAID : PaymentStatus.PENDING);
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

    public PaymentStatus getPaymentStatus() {
      return status;
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

    public void setAmount(double amount) {
      this.amount = amount;
    }

    public void setPaymentDate(LocalDate paymentDate) {
      this.paymentDate = paymentDate;
    }
    
    public void setPaymentDeadline(LocalDate paymentDeadline) {
      this.paymentDeadline = paymentDeadline;
    }

    public void setPaymentStatus(boolean paid) {
      this.status = (paid ? PaymentStatus.PAID : PaymentStatus.PENDING);
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
