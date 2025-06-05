package at.fhv.messaging.event;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;
import java.util.List;

@Serdeable
public record TermUpdatedEvent(
        String termId,
        String eventId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String location,
        String meetingPoint,
        int minParticipants,
        int maxParticipants,
        double price,
        List<String> caregivers
) {}