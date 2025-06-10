package at.fhv.messaging.event;

import at.fhv.model.PaymentStatus;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record SponsorPaymentUpdatedEvent(
        String sponsorId,
        String organizationId,
        PaymentStatus paymentStatus) { }

