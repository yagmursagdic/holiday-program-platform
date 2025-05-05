package at.fhv.controller;

import at.fhv.messaging.TermEventProducer;
import at.fhv.messaging.event.*;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller("/term")
public class TermController {

    //private final UserService userService;

    @Inject
    TermEventProducer producer;

    @Get("/hello")
    public String hello() {
        return "Hello from TermService";
    }

    @Post("/create")
    public HttpResponse<?> createTerm(@Body Map<String, Object> body) {
        String termId = (String) body.get("termId");
        String eventId = (String) body.get("eventId");
        LocalDate date = LocalDate.parse((String) body.get("date"));
        LocalTime time = LocalTime.parse((String) body.get("time"));
        String location = (String) body.get("location");
        String meetingPoint = (String) body.get("meetingPoint");
        int minParticipants = ((Number) body.get("minParticipants")).intValue();
        int maxParticipants = ((Number) body.get("maxParticipants")).intValue();
        double price = ((Number) body.get("price")).doubleValue();
        List<String> caregivers = (List<String>) body.get("caregivers");

        producer.sendTermCreated(termId, new TermCreatedEvent(
                termId, eventId, date, time, location, meetingPoint,
                minParticipants, maxParticipants, price, caregivers
        ));

        return HttpResponse.ok("Term created + event sent.");
    }

    @Put("/update/{termId}")
    public HttpResponse<?> updateTerm(@PathVariable String termId, @Body Map<String, Object> body) {
        LocalDate date = LocalDate.parse((String) body.get("date"));
        LocalTime time = LocalTime.parse((String) body.get("time"));
        String location = (String) body.get("location");
        String meetingPoint = (String) body.get("meetingPoint");
        int minParticipants = ((Number) body.get("minParticipants")).intValue();
        int maxParticipants = ((Number) body.get("maxParticipants")).intValue();
        double price = ((Number) body.get("price")).doubleValue();
        List<String> caregivers = (List<String>) body.get("caregivers");

        producer.sendTermUpdated(termId, new TermUpdatedEvent(
                termId, date, time, location, meetingPoint,
                minParticipants, maxParticipants, price, caregivers
        ));

        return HttpResponse.ok("Term updated + event sent.");
    }

    @Delete("/delete/{termId}")
    public HttpResponse<?> deleteTerm(@PathVariable String termId) {
        producer.sendTermDeleted(termId, new TermDeletedEvent(termId));
        return HttpResponse.ok("Term deleted + event sent.");
    }

    @Post("/assign-caregiver")
    public HttpResponse<?> assignCaregiver(@Body Map<String, Object> body) {
        String termId = (String) body.get("termId");
        String caregiverId = (String) body.get("caregiverId");

        producer.sendCaregiverAssigned(termId, new CaregiverAssignedEvent(termId, caregiverId));
        return HttpResponse.ok("Caregiver assigned + event sent.");
    }

    @Post("/unassign-caregiver/{termId}")
    public HttpResponse<?> unassignCaregiver(@PathVariable String termId) {
        producer.sendCaregiverUnassigned(termId, new CaregiverUnassignedEvent(termId));
        return HttpResponse.ok("Caregiver unassigned + event sent.");
    }
}
