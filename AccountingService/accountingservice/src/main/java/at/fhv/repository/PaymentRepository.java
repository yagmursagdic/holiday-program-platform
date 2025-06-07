package at.fhv.repository;

import java.util.List;

import at.fhv.model.Payment;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {

  List<Payment> findByTermId(String termId);

    // Keine zusätzlichen Methoden nötig für den aktuellen Service

}

