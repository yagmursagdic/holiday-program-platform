package at.fhv.repository;

import java.util.List;

import at.fhv.model.Payment;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UserPaymentRepository extends CrudRepository<Payment, String> {

  List<Payment> findByTermId(String termId);

  List<Payment> findOpenPaymentsByTermId(String termId);

  List<Payment> findOpenPaymentsByUserId(String userId);

  List<Payment> findPaymentStatusByTermId(String userId, String termId);
}

