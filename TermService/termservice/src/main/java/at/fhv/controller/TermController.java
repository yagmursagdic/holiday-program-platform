package at.fhv.controller;

import at.fhv.messaging.TermEventProducer;
import at.fhv.messaging.event.*;
import at.fhv.model.Term;
import at.fhv.service.TermCommandService;
import at.fhv.service.TermQueryService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Optional;


// Controller for managing Terms
@Controller("/terms")
public class TermController {

    private static final Logger LOG = LoggerFactory.getLogger(TermController.class);

    @Inject
    TermCommandService termCommandService;

    @Inject
    TermQueryService termQueryService;

    @Inject
    TermEventProducer producer;

    // Simple health check endpoint
    @Get("/hello")
    public String hello() {
        return "Hello from TermService";
    }

    // Create a new term and emit event
    @Post("/create")
    public HttpResponse<?> createTerm(@Body @Valid Term term) {
        try {
            Term createdTerm = termCommandService.createTerm(term);

            producer.sendTermCreated(createdTerm.getTermId(), new TermCreatedEvent(
                    createdTerm.getTermId(),
                    createdTerm.getEventId(),
                    createdTerm.getStartTime(),
                    createdTerm.getEndTime(),
                    createdTerm.getLocation(),
                    createdTerm.getMeetingPoint(),
                    createdTerm.getMinParticipants(),
                    createdTerm.getMaxParticipants(),
                    createdTerm.getPrice(),
                    createdTerm.getCaregiverIds()
            ));

            return HttpResponse.created(createdTerm.getTermId());
        } catch (Exception e) {
            LOG.error("Failed to create term", e);
            return HttpResponse.serverError("Could not create term.");
        }
    }

    // Get a term by its ID
    @Get("/term/{termId}")
    public HttpResponse<?> getTerm(@PathVariable String termId) {
        try {
            Optional<Term> term = termQueryService.getTermById(termId);
            return term.map(HttpResponse::ok)
                    .orElseGet(() -> HttpResponse.notFound());
        } catch (Exception e) {
            LOG.error("Error retrieving term with ID {}", termId, e);
            return HttpResponse.serverError("Error retrieving term.");
        }
    }

    // Get all terms for a specific event
    @Get("/{eventId}")
    public HttpResponse<?> getAllTermsByEventId(@PathVariable String eventId) {
        try {
            List<Term> terms = termQueryService.getAllTermsByEventId(eventId);
            return HttpResponse.ok(terms);
        } catch (Exception e) {
            LOG.error("Error retrieving terms for event ID {}", eventId, e);
            return HttpResponse.serverError("Error retrieving terms.");
        }
    }

    // Update an existing term and emit event
    @Put("/update/{termId}")
    public HttpResponse<?> updateTerm(@PathVariable String termId, @Body Term updatedTerm) {
        try {
            Optional<Term> updated = termCommandService.updateTerm(termId, updatedTerm);
            if (updated.isPresent()) {
                Term term = updated.get();

                producer.sendTermUpdated(term.getTermId(), new TermUpdatedEvent(
                        term.getTermId(),
                        term.getEventId(),
                        term.getStartTime(),
                        term.getEndTime(),
                        term.getLocation(),
                        term.getMeetingPoint(),
                        term.getMinParticipants(),
                        term.getMaxParticipants(),
                        term.getPrice(),
                        term.getCaregiverIds()
                ));

                return HttpResponse.ok(term.getTermId());
            } else {
                return HttpResponse.notFound("Term with ID " + termId + " not found.");
            }
        } catch (Exception e) {
            LOG.error("Error updating term with ID {}", termId, e);
            return HttpResponse.serverError("Could not update term.");
        }
    }

    // Delete a term and emit event
    @Delete("/delete/{termId}")
    public HttpResponse<?> deleteTerm(@PathVariable String termId) {
        try {
            termCommandService.deleteTerm(termId);
            producer.sendTermDeleted(termId, new TermDeletedEvent(termId));
            return HttpResponse.ok(termId);
        } catch (Exception e) {
            LOG.error("Error deleting term with ID {}", termId, e);
            return HttpResponse.serverError("Could not delete term.");
        }
    }

    // Assign a caregiver to a term and emit event
    @Post("/assign-caregiver/{termId}/{caregiverId}")
    public HttpResponse<?> assignCaregiver(@PathVariable String termId, @PathVariable String caregiverId) {
        try {
            termCommandService.assignCaregiver(termId, caregiverId);
            producer.sendCaregiverAssigned(termId, new CaregiverAssignedEvent(termId, caregiverId));
            return HttpResponse.ok(caregiverId);
        } catch (Exception e) {
            LOG.error("Error assigning caregiver {} to term {}", caregiverId, termId, e);
            return HttpResponse.serverError("Could not assign caregiver.");
        }
    }

    // Unassign a caregiver from a term and emit event
    @Post("/unassign-caregiver/{termId}/{caregiverId}")
    public HttpResponse<?> unassignCaregiver(@PathVariable String termId, @PathVariable String caregiverId) {
        try {
            termCommandService.unassignCaregiver(termId, caregiverId);
            producer.sendCaregiverUnassigned(termId, new CaregiverUnassignedEvent(termId, caregiverId));
            return HttpResponse.ok(caregiverId);
        } catch (Exception e) {
            LOG.error("Error unassigning caregiver {} from term {}", caregiverId, termId, e);
            return HttpResponse.serverError("Could not unassign caregiver.");
        }
    }
}
