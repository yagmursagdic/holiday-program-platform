package at.fhv.repository;

import at.fhv.model.Term;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface TermRepository extends CrudRepository<Term, String> {

    List<Term> findByEventId(String eventId);

    // Keine zusätzlichen Methoden nötig für den aktuellen Service

}

