package at.fhv.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import java.util.Objects;

@Serdeable
@Entity
@Table(name = "sponsor_payments")
public class SponsorPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String sponsorPaymentId;

    private String sponsorId;

    private String organizationId;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public SponsorPayment() {
        // Standard-Constructor for JPA
    }

    public SponsorPayment(String sponsorId, Double amount, PaymentStatus paymentStatus) {
        this.sponsorId = sponsorId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public String getSponsorPaymentId() {
        return sponsorPaymentId;
    }

    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SponsorPayment)) return false;
        SponsorPayment that = (SponsorPayment) o;
        return Objects.equals(sponsorPaymentId, that.sponsorPaymentId);
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
