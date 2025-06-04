package at.fhv.service;

import at.fhv.exception.DuplicateAssignmentException;
import at.fhv.exception.TermNotFoundException;
import at.fhv.model.Term;
import at.fhv.repository.TermRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class TermCommandService {

    private final TermRepository termRepository;

    public TermCommandService(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    public Term createTerm(Term term) {
        return termRepository.save(term);
    }

    public Optional<Term> updateTerm(String id, Term updatedTerm) {
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
            return termRepository.update(existing);
        });
    }

    public void deleteTerm(String id) {
        termRepository.deleteById(id);
    }

    public void assignCaregiver(String termId, String caregiverId) {
        Optional<Term> optionalTerm = termRepository.findById(termId);

        if (optionalTerm.isEmpty()) {
            throw new TermNotFoundException(termId);
        }

        Term term = optionalTerm.get();
        List<String> caregiverIds = term.getCaregiverIds();

        if (caregiverIds.contains(caregiverId)) {
            throw new DuplicateAssignmentException(caregiverId);
        }

        if (!caregiverIds.contains(caregiverId)) {
            caregiverIds.add(caregiverId);
            term.setCaregiverIds(caregiverIds);
            termRepository.update(term);
        }
    }

    public void unassignCaregiver(String termId, String caregiverId) {
        Optional<Term> optionalTerm = termRepository.findById(termId);

        if (optionalTerm.isEmpty()) {
            throw new TermNotFoundException(termId);
        }

        Term term = optionalTerm.get();
        List<String> caregiverIds = term.getCaregiverIds();

        if (caregiverIds.contains(caregiverId)) {
            caregiverIds.remove(caregiverId);
            term.setCaregiverIds(caregiverIds);
            termRepository.update(term);
        }
    }

}
