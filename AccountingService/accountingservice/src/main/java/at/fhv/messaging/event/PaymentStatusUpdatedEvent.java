package at.fhv.messaging.event;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record PaymentStatusUpdatedEvent(
        String paymentId,
        String userId,
        String termId,
        String newStatus
) {}