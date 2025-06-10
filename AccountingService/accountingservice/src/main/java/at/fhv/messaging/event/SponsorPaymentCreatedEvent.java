package at.fhv.messaging.event;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record SponsorPaymentCreatedEvent (
        String sponsorId,
        String organizationId,
        Double amount) { }

