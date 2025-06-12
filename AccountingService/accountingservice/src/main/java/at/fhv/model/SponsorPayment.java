package at.fhv.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Serdeable
@Entity
@Table(name = "sponsor_payments")
public class SponsorPayment {

    @Id
    @Column(name = "sponsor_payment_id")
    private String sponsorPaymentId;

    @PrePersist
    public void generateId() {
        if (sponsorPaymentId == null) {
            sponsorPaymentId = UUID.randomUUID().toString();
        }
    }
    private String sponsorId;
    private String organizationId;
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public SponsorPayment() {
        // Standard-Constructor for JPA
    }

    public SponsorPayment(String sponsorId,  String organizationId, Double amount, boolean paid) {
        this.sponsorId = sponsorId;
        this.organizationId = organizationId;
        this.amount = amount;
        this.paymentStatus = (paid ? PaymentStatus.PAID : PaymentStatus.PENDING);
    }

    // getters
    public String getSponsorPaymentId() {
        return sponsorPaymentId;
    }

    public String getSponsorId() {
      return sponsorId;
    }
    
    public String getOrganizationId() {
      return organizationId;
    }
    
    public Double getAmount() {
      return amount;
    }
    
    public PaymentStatus getPaymentStatus() {
      return paymentStatus;
    }

    // setters
    public void setSponsorPaymentId(String sponsorPaymentId) {
      this.sponsorPaymentId = sponsorPaymentId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPaymentStatus(boolean paid) {
        this.paymentStatus = (paid ? PaymentStatus.PAID : PaymentStatus.PENDING);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SponsorPayment)) return false;
        SponsorPayment sponsorPayment = (SponsorPayment) o;
        return Objects.equals(sponsorPaymentId, sponsorPayment.sponsorPaymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sponsorPaymentId);
    }

    @Override
    public String toString() {
        return "SponsorPayment{" +
                "sponsorPaymentId=" + sponsorPaymentId +
                ", sponsorId=" + sponsorId +
                ", organizationId=" + organizationId +
                ", amount=" + amount +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
