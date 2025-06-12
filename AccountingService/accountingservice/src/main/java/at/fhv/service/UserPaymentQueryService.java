package at.fhv.service;

import at.fhv.exception.PaymentNotFoundException;
import at.fhv.model.Payment;
import at.fhv.model.PaymentStatus;
import at.fhv.repository.UserPaymentRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class UserPaymentQueryService {

    private final UserPaymentRepository userPaymentRepository;

    public UserPaymentQueryService(UserPaymentRepository userPaymentRepository) {
        this.userPaymentRepository = userPaymentRepository;
    }

    public Optional<Payment> getPaymentById(String paymentId) {
        return userPaymentRepository.findById(paymentId).or(() -> {
            throw new PaymentNotFoundException(paymentId);
        });
    }

    public List<Payment> getAllPaymentsByTermId(String termId) {
        return userPaymentRepository.findByTermId(termId);
    }

    public List<Payment> getAllOpenPaymentsByTermId(String termId) {
        return userPaymentRepository.findByTermIdAndStatus(termId, PaymentStatus.OPEN);
    }

    public List<Payment> getAllOpenPaymentsByUserId(String userId) {
        return userPaymentRepository.findByUserIdAndStatus(userId, PaymentStatus.OPEN);
    }

}
