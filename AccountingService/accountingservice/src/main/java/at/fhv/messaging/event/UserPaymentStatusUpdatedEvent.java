package at.fhv.messaging.event;

import at.fhv.model.PaymentStatus;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UserPaymentStatusUpdatedEvent(
        String paymentId,
        PaymentStatus newStatus
) {}