package at.fhv.messaging.event;

import at.fhv.model.PaymentStatus;

import java.time.LocalDate;

public class UserPaymentUpdatedEvent {
    public UserPaymentUpdatedEvent(String userId, String termId, double amount, LocalDate paymentDate, LocalDate paymentDeadline, PaymentStatus paymentStatus) {
    }
}
