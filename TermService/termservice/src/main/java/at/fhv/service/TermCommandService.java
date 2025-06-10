package at.fhv.service;

import at.fhv.exception.DuplicateAssignmentException;
import at.fhv.exception.TermNotFoundException;
import at.fhv.messaging.TermEventProducer;
import at.fhv.messaging.event.*;
import at.fhv.model.Term;
import at.fhv.repository.TermRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class TermCommandService {

    private final TermRepository termRepository;
    private final TermEventProducer producer;

    public TermCommandService(TermRepository termRepository, TermEventProducer producer) {
        this.termRepository = termRepository;
        this.producer = producer;
    }

    public Term createTerm(Term term) {
        try {
            Term created = termRepository.save(term);

            producer.sendTermCreated(created.getTermId(), new TermCreatedEvent(
                    created.getTermId(),
                    created.getEventId(),
                    created.getStartTime(),
                    created.getEndTime(),
                    created.getLocation(),
                    created.getMeetingPoint(),
                    created.getMinParticipants(),
                    created.getMaxParticipants(),
                    created.getPrice(),
                    created.getCaregiverIds()
            ));

            return created;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create term", e);
        }
    }

    public Optional<Term> updateTerm(String id, Term updatedTerm) {
        try {
            return termRepository.findById(id).map(existing -> {
                existing.setEventId(updatedTerm.getEventId());
                existing.setStartTime(updatedTerm.getStartTime());
                existing.setEndTime(updatedTerm.getEndTime());
                existing.setLocation(updatedTerm.getLocation());
                existing.setMeetingPoint(updatedTerm.getMeetingPoint());
                existing.setMinParticipants(updatedTerm.getMinParticipants());
                existing.setMaxParticipants(updatedTerm.getMaxParticipants());
                existing.setPrice(updatedTerm.getPrice());
                existing.setCaregiverIds(updatedTerm.getCaregiverIds());

                Term updated = termRepository.update(existing);

                producer.sendTermUpdated(updated.getTermId(), new TermUpdatedEvent(
                        updated.getTermId(),
                        updated.getEventId(),
                        updated.getStartTime(),
                        updated.getEndTime(),
                        updated.getLocation(),
                        updated.getMeetingPoint(),
                        updated.getMinParticipants(),
                        updated.getMaxParticipants(),
                        updated.getPrice(),
                        updated.getCaregiverIds()
                ));

                return updated;
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to update term with id: " + id, e);
        }
    }

    public void deleteTerm(String id) {
        try {
            termRepository.deleteById(id);
            producer.sendTermDeleted(id, new TermDeletedEvent(id));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete term with id: " + id, e);
        }
    }

    public void assignCaregiver(String termId, String caregiverId) {
        try {
            Term term = termRepository.findById(termId).orElseThrow(() -> new TermNotFoundException(termId));
            List<String> caregiverIds = term.getCaregiverIds();

            if (caregiverIds.contains(caregiverId)) {
                throw new DuplicateAssignmentException(caregiverId);
            }

            caregiverIds.add(caregiverId);
            term.setCaregiverIds(caregiverIds);
            termRepository.update(term);

            producer.sendCaregiverAssigned(termId, new CaregiverAssignedEvent(termId, caregiverId));
        } catch (TermNotFoundException | DuplicateAssignmentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to assign caregiver " + caregiverId + " to term " + termId, e);
        }
    }

    public void unassignCaregiver(String termId, String caregiverId) {
        try {
            Term term = termRepository.findById(termId).orElseThrow(() -> new TermNotFoundException(termId));
            List<String> caregiverIds = term.getCaregiverIds();

            if (caregiverIds.remove(caregiverId)) {
                term.setCaregiverIds(caregiverIds);
                termRepository.update(term);
                producer.sendCaregiverUnassigned(termId, new CaregiverUnassignedEvent(termId, caregiverId));
            }
        } catch (TermNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to unassign caregiver " + caregiverId + " from term " + termId, e);
        }
    }
}

