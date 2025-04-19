package at.fhv.model;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_records")
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;     
    private Long termId;     

    private boolean paid;

    public PaymentRecord() {}

}
