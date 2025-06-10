package at.fhv.controller;

import at.fhv.exception.DuplicateAssignmentException;
import at.fhv.exception.TermNotFoundException;
import at.fhv.model.Term;
import at.fhv.service.TermCommandService;
import at.fhv.service.TermQueryService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller("/terms")
public class TermController {

    private static final Logger LOG = LoggerFactory.getLogger(TermController.class);
    private final TermCommandService termCommandService;
    private final TermQueryService termQueryService;

    public TermController(TermCommandService termCommandService, TermQueryService termQueryService) {
        this.termCommandService = termCommandService;
        this.termQueryService = termQueryService;
    }

    @Get("/hello")
    public String hello() {
        return "Hello from TermService";
    }

    @Post()
    public HttpResponse<?> createTerm(@Body @Valid Term term) {
        try {
            Term createdTerm = termCommandService.createTerm(term);
            return HttpResponse.created(createdTerm);
        } catch (Exception e) {
            LOG.error("Failed to create term", e);
            return HttpResponse.serverError("Could not create term.");
        }
    }

    @Get("/{termId}")
    public HttpResponse<?> getTerm(@PathVariable String termId) {
        try {
            Optional<Term> term = termQueryService.getTermById(termId);
            return term.map(HttpResponse::ok)
                    .orElseGet(HttpResponse::notFound);
        } catch (Exception e) {
            LOG.error("Error retrieving term with ID {}", termId, e);
            return HttpResponse.serverError("Error retrieving term.");
        }
    }

    @Get("/by-event/{eventId}")
    public HttpResponse<?> getAllTermsByEventId(@PathVariable String eventId) {
        try {
            List<Term> terms = termQueryService.getAllTermsByEventId(eventId);
            return HttpResponse.ok(terms);
        } catch (Exception e) {
            LOG.error("Error retrieving terms for event ID {}", eventId, e);
            return HttpResponse.serverError("Error retrieving terms.");
        }
    }

    @Put("/{termId}")
    public HttpResponse<?> updateTerm(@PathVariable String termId, @Body @Valid Term updatedTerm) {
        try {
            Optional<Term> updated = termCommandService.updateTerm(termId, updatedTerm);
            return updated
                    .map(HttpResponse::ok)
                    .orElseGet(HttpResponse::notFound);
        } catch (Exception e) {
            LOG.error("Error updating term with ID {}", termId, e);
            return HttpResponse.serverError("Could not update term.");
        }
    }

    @Delete("/{termId}")
    public HttpResponse<?> deleteTerm(@PathVariable String termId) {
        try {

            termCommandService.deleteTerm(termId);
            return HttpResponse.ok(termId);
        } catch (Exception e) {
            LOG.error("Error deleting term with ID {}", termId, e);
            return HttpResponse.serverError("Could not delete term.");
        }
    }

    @Post("/assign-caregiver/{termId}/{caregiverId}")
    public HttpResponse<?> assignCaregiver(@PathVariable String termId, @PathVariable String caregiverId) {
        try {
            termCommandService.assignCaregiver(termId, caregiverId);
            return HttpResponse.ok(Map.of(
                    "termId", termId,
                    "caregiverId", caregiverId,
                    "status", "assigned"
            ));
        } catch (DuplicateAssignmentException e) {
            LOG.error("Duplicate assignment for caregiver {} to term {}", caregiverId, termId, e);
            return HttpResponse.status(HttpStatus.CONFLICT).body("Caregiver already assigned.");
        } catch (TermNotFoundException e) {
            LOG.error("Term not found for ID {}", termId, e);
            return HttpResponse.notFound("Term not found.");
        } catch (Exception e) {
            LOG.error("Error assigning caregiver {} to term {}", caregiverId, termId, e);
            return HttpResponse.serverError("Could not assign caregiver.");
        }
    }

    @Post("/unassign-caregiver/{termId}/{caregiverId}")
    public HttpResponse<?> unassignCaregiver(@PathVariable String termId, @PathVariable String caregiverId) {
        try {
            termCommandService.unassignCaregiver(termId, caregiverId);
            return HttpResponse.ok(Map.of(
                    "termId", termId,
                    "caregiverId", caregiverId,
                    "status", "unassigned"
            ));
        } catch (TermNotFoundException e) {
            LOG.error("Term not found for ID {}", termId, e);
            return HttpResponse.notFound("Term not found.");
        } catch (Exception e) {
            LOG.error("Error unassigning caregiver {} from term {}", caregiverId, termId, e);
            return HttpResponse.serverError("Could not unassign caregiver.");
        }
    }
}
