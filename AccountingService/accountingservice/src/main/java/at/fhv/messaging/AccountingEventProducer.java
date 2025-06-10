package at.fhv.messaging;

import at.fhv.messaging.event.*;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface AccountingEventProducer {


    @Topic("accounting.user.payments.created")
    void sendUserPaymentCreated(String paymentId, UserPaymentCreatedEvent userPaymentCreatedEvent);

    @Topic("accounting.user.payments.statusUpdated")
    void sendUserPaymentStatusUpdated(@KafkaKey String key, UserPaymentStatusUpdatedEvent event);

    @Topic("accounting.user.payments.updated")
    void sendUserPaymentUpdated(String termId, UserPaymentUpdatedEvent userPaymentUpdatedEvent);

    @Topic("accounting.user.payments.deleted")
    void sendUserPaymentDeleted(@KafkaKey String key, UserPaymentDeletedEvent event);

    @Topic("accounting.user.payments.received")
    void sendPaymentReceived(@KafkaKey String key, UserPaymentReceivedEvent event);

    @Topic("accounting.sponsor.payments.created")
    void sendSponsorPaymentCreated(String sponsorPaymentId, SponsorPaymentCreatedEvent sponsorPaymentCreatedEvent);

    @Topic("accounting.sponsor.payments.updated")
    void sendSponsorPaymentUpdated(String sponsorPaymentId, SponsorPaymentUpdatedEvent sponsorPaymentUpdatedEvent);

    void sendSponsorPaymentDeleted(String sponsorPaymentId, SponsorPaymentDeletedEvent sponsorPaymentDeletedEvent);
}