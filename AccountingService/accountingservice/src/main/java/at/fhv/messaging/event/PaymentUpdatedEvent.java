package at.fhv.messaging.event;

import at.fhv.model.PaymentStatus;

import java.time.LocalDate;

public class PaymentUpdatedEvent {
    public PaymentUpdatedEvent(String userId, String termId, double amount, LocalDate paymentDate, LocalDate paymentDeadline, PaymentStatus paymentStatus) {
    }
}
