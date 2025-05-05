package at.fhv.messaging;


import at.fhv.messaging.event.PaymentCreatedEvent;
import at.fhv.messaging.event.PaymentDeletedEvent;
import at.fhv.messaging.event.PaymentReceivedEvent;
import at.fhv.messaging.event.PaymentStatusUpdatedEvent;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface AccountingEventProducer {

    @Topic("accounting.payments.created")
    void sendPaymentCreated(@KafkaKey String key, PaymentCreatedEvent event);

    @Topic("accounting.payments.statusUpdated")
    void sendPaymentStatusUpdated(@KafkaKey String key, PaymentStatusUpdatedEvent event);

    @Topic("accounting.payments.deleted")
    void sendPaymentDeleted(@KafkaKey String key, PaymentDeletedEvent event);

    @Topic("accounting.payments.received")
    void sendPaymentReceived(@KafkaKey String key, PaymentReceivedEvent event);

}