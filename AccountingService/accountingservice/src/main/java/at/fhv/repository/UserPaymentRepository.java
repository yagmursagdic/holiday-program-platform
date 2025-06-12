package at.fhv.repository;

import java.util.List;

import at.fhv.model.Payment;
import at.fhv.model.PaymentStatus;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UserPaymentRepository extends CrudRepository<Payment, String> {

  List<Payment> findByTermId(String termId);

  List<Payment> findByTermIdAndStatus(String termId, PaymentStatus status);

  List<Payment> findByUserIdAndStatus(String userId, PaymentStatus status);
}

