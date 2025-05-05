package at.fhv.messaging.event;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record PaymentDeletedEvent(
        String paymentId
) {}