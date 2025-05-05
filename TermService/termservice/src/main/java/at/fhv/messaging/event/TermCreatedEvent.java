package at.fhv.messaging.event;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Serdeable
public record TermCreatedEvent(
        String termId,
        String eventId,
        LocalDate date,
        LocalTime time,
        String location,
        String meetingPoint,
        int minParticipants,
        int maxParticipants,
        double price,
        List<String> caregivers
) {}