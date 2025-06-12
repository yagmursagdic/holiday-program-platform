package at.fhv.service;

import at.fhv.exception.PaymentNotFoundException;
import at.fhv.messaging.AccountingEventProducer;
import at.fhv.messaging.event.UserPaymentDeletedEvent;
import at.fhv.messaging.event.UserPaymentStatusUpdatedEvent;
import at.fhv.messaging.event.UserPaymentUpdatedEvent;
import at.fhv.messaging.event.UserPaymentCreatedEvent;
import at.fhv.model.Payment;
import at.fhv.model.PaymentStatus;
import at.fhv.repository.UserPaymentRepository;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class UserPaymentCommandService {

    private final UserPaymentRepository userPaymentRepository;
    private final AccountingEventProducer producer;

    public UserPaymentCommandService(UserPaymentRepository userPaymentRepository, AccountingEventProducer producer) {
        this.userPaymentRepository = userPaymentRepository;
        this.producer = producer;
    }

    public Payment createUserPayment(Payment payment) {
        try {
            Payment created = userPaymentRepository.save(payment);

            producer.sendUserPaymentCreated(
                    created.getPaymentId(),
                    new UserPaymentCreatedEvent(
                            created.getPaymentId(),
                            created.getUserId(),
                            created.getTermId(),
                            created.getAmount(),
                            created.getPaymentDate(),
                            created.getPaymentDeadline(),
                            created.getStatus()));

            return created;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user payment", e);
        }

    }

    public Optional<Payment> updateUserPayment(String userPaymentId, Payment payment) {
        try {
            return userPaymentRepository.findById(userPaymentId).map(existing -> {
                existing.setUserId(payment.getUserId());
                existing.setTermId(payment.getTermId());
                existing.setAmount(payment.getAmount());
                existing.setPaymentDate(payment.getPaymentDate());
                existing.setPaymentDeadline(payment.getPaymentDeadline());
                existing.setStatus(payment.getStatus());

                Payment updated = userPaymentRepository.update(existing);

                producer.sendUserPaymentUpdated(updated.getTermId(), new UserPaymentUpdatedEvent(
                        updated.getUserId(),
                        updated.getTermId(),
                        updated.getAmount(),
                        updated.getPaymentDate(),
                        updated.getPaymentDeadline(),
                        updated.getStatus()
                ));

                return updated;
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user payment with id: " + userPaymentId, e);
        }
    }

    public void deleteUserPayment(String userPaymentId) {
        try {
            userPaymentRepository.deleteById(userPaymentId);
            producer.sendUserPaymentDeleted(userPaymentId, new UserPaymentDeletedEvent(userPaymentId));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user payment with id: " + userPaymentId, e);
        }
    }

    public void notifyPaymentReceived(String userId, String termId, double amount) {
        // payment id - payment holen betrag anpassen, wenn betrag gleich 0, status auf paid, wenn amount = 0 dann zahlung nicht mehr möglich
    }

    public void notifyPaymentPending(String userId, String termId, Double price, String paymentDeadline) {
    }

    public boolean markUserPaymentAsPaid(String paymentId) {
        try{
            Payment payment = userPaymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
            payment.setStatus(PaymentStatus.PAID);
            userPaymentRepository.update(payment);

            producer.sendUserPaymentStatusUpdated(paymentId, new UserPaymentStatusUpdatedEvent(paymentId, payment.getStatus()));
            //notify aufrufen
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to mark payment as paid " + paymentId, e);
        }
    }
}
