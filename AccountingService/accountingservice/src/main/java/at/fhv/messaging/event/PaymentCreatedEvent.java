package at.fhv.messaging.event;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;

@Serdeable
public record PaymentCreatedEvent(
        String paymentId,
        String userId,
        String termId,
        double amount,
        LocalDate paymentDeadline
) {}

