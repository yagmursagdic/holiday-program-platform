package at.fhv.messaging;

import at.fhv.messaging.event.*;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface TermEventProducer {

    @Topic("term.created")
    void sendTermCreated(@KafkaKey String key, TermCreatedEvent event);

    @Topic("term.updated")
    void sendTermUpdated(@KafkaKey String key, TermUpdatedEvent event);

    @Topic("term.deleted")
    void sendTermDeleted(@KafkaKey String key, TermDeletedEvent event);

    @Topic("term.caregiver.assigned")
    void sendCaregiverAssigned(@KafkaKey String key, CaregiverAssignedEvent event);

    @Topic("term.caregiver.unassigned")
    void sendCaregiverUnassigned(@KafkaKey String key, CaregiverUnassignedEvent event);
}