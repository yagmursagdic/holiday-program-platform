package at.fhv.repository;

import java.util.List;

import at.fhv.model.SponsorPayment;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface SponsorPaymentRepository extends CrudRepository<SponsorPayment, String> {

  List<SponsorPayment> findBySponsorId(String sponsotId);
  
  // Keine zusätzlichen Methoden nötig für den aktuellen Service

}

