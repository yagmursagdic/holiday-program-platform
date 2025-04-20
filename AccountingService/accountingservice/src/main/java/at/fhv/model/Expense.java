package at.fhv.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

@Serdeable
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long termId;

    private Double amount;

    private boolean paid;

    public Expense() {
        // Standard-Konstruktor für JPA
    }

    public Expense(Long termId, Double amount, boolean paid) {
        this.termId = termId;
        this.amount = amount;
        this.paid = paid;
    }

    // Getter und Setter

    public Long getId() {
        return id;
    }

    public Long getTermId() {
        return termId;
    }

    public void setTermId(Long termId) {
        this.termId = termId;
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
