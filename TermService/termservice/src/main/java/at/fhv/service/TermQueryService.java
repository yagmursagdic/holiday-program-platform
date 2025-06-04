package at.fhv.service;

import at.fhv.model.Term;
import at.fhv.repository.TermRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class TermQueryService {

    private TermRepository termRepository;

    public TermQueryService(TermRepository termRepository) {
        this.termRepository = termRepository;
    }
    public Optional<Term> getTermById(String id) {
        return termRepository.findById(id);
    }

    public List<Term> getAllTermsByEventId(String eventId) { return termRepository.findByEventId(eventId); }

}
