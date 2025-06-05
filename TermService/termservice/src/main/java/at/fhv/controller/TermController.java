package at.fhv.controller;

import at.fhv.messaging.TermEventProducer;
import at.fhv.messaging.event.*;
import at.fhv.model.Term;
import at.fhv.service.TermCommandService;
import at.fhv.service.TermQueryService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@Controller("/term")
public class TermController {

    @Inject
    TermCommandService termCommandService;

    @Inject
    TermQueryService termQueryService;

    @Inject
    TermEventProducer producer;

    @Get("/hello")
    public String hello() {
        return "Hello from TermService";
    }

    // micronaut macht deserialisierung automatisch
    @Post("/create")
    public HttpResponse<?> createTerm(@Body Term term) {
        Term createdTerm = termCommandService.createTerm(term);

        producer.sendTermCreated(
                createdTerm.getTermId(),
                new TermCreatedEvent(
                        createdTerm.getTermId(),
                        createdTerm.getEventId(),
                        createdTerm.getStartTime(),
                        createdTerm.getEndTime(),
                        createdTerm.getLocation(),
                        createdTerm.getMeetingPoint(),
                        createdTerm.getMinParticipants(),
                        createdTerm.getMaxParticipants(),
                        createdTerm.getPrice(),
                        createdTerm.getCaregiverIds()));

        return HttpResponse.ok("Term: " + createdTerm.getTermId() + " created and event sent.");
    }

    @Get("/{termId}")
    public HttpResponse<?> getTerm(@PathVariable String termId) {
        Optional<Term> term = termQueryService.getTermById(termId);
        return term.map(HttpResponse::ok).orElseGet(() -> HttpResponse.notFound());
    }

    @Get("/{eventId}")
    public List<Term> getAllTermsByEventId(@PathVariable String eventId) {
        return termQueryService.getAllTermsByEventId(eventId);
    }

    @Put("/update/{termId}")
    public HttpResponse<?> updateTerm(@PathVariable String termId, @Body Term updatedTerm) {
        Optional<Term> updated = termCommandService.updateTerm(termId, updatedTerm);
        if (updated.isPresent()) {
            Term term = updated.get();

            producer.sendTermUpdated(
                    term.getTermId(),
                    new TermUpdatedEvent(
                            term.getTermId(),
                            term.getEventId(),
                            term.getStartTime(),
                            term.getEndTime(),
                            term.getLocation(),
                            term.getMeetingPoint(),
                            term.getMinParticipants(),
                            term.getMaxParticipants(),
                            term.getPrice(),
                            term.getCaregiverIds()));

            return HttpResponse.ok("Term " + term.getTermId() + " updated and event sent");
        } else {
            return HttpResponse.notFound();
        }
    }

    @Delete("/delete/{termId}")
    public HttpResponse<?> deleteTerm(@PathVariable String termId) {
        termCommandService.deleteTerm(termId);
        producer.sendTermDeleted(termId, new TermDeletedEvent(termId));
        return HttpResponse.ok("Term " + termId + " deleted and event sent.");
    }

    @Post("/assign-caregiver/{termId}/{caregiverId}")
    public HttpResponse<?> assignCaregiver(@PathVariable String termId, @PathVariable String caregiverId) {
        termCommandService.assignCaregiver(termId, caregiverId);
        producer.sendCaregiverAssigned(termId, new CaregiverAssignedEvent(termId, caregiverId));
        return HttpResponse.ok("Caregiver " + caregiverId + " assigned and event sent.");
    }

    @Post("/unassign-caregiver/{termId}/{caregiverId}")
    public HttpResponse<?> unassignCaregiver(@PathVariable String termId, @PathVariable String caregiverId) {
        termCommandService.unassignCaregiver(termId, caregiverId);
        producer.sendCaregiverUnassigned(termId, new CaregiverUnassignedEvent(termId, caregiverId));
        return HttpResponse.ok("Caregiver " + caregiverId + " unassigned and event sent.");
    }
}
