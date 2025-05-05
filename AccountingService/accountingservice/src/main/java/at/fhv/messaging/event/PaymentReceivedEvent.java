package at.fhv.messaging.event;


import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;

@Serdeable
public record PaymentReceivedEvent(
        String userId,
        String termId,
        double amount,
        LocalDate receivedAt
) {}