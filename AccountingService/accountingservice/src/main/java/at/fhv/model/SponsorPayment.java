package at.fhv.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

@Serdeable
@Entity
@Table(name = "sponsor_payments")
public class SponsorPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sponsorId;

    private Double amount;

    private boolean paid;

    public SponsorPayment() {
        // Standard-Konstruktor für JPA
    }

    public SponsorPayment(Long sponsorId, Double amount, boolean paid) {
        this.sponsorId = sponsorId;
        this.amount = amount;
        this.paid = paid;
    }

    // Getter und Setter (wichtig für Micronaut/JPA)

    public Long getId() {
        return id;
    }

    public Long getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(Long sponsorId) {
        this.sponsorId = sponsorId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}

