package at.fhv.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

@Serdeable
@Entity
@Table(name = "payment_records")
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;     
    private Long termId;     

    private boolean paid;

    public PaymentRecord() {
        // Standard-Konstruktor für JPA
    }

    public PaymentRecord(Long termId, Long userId, boolean paid) {
        this.termId = termId;
        this.userId = userId;
        this.paid = paid;
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

    public boolean getPaid() {
      return paid;
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

    public void setPaid(boolean paid) {
      this.paid = paid;
    }


}
