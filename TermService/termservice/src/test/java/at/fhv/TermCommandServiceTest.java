package at.fhv;

import at.fhv.exception.DuplicateAssignmentException;
import at.fhv.exception.TermNotFoundException;
import at.fhv.messaging.TermEventProducer;
import at.fhv.messaging.event.CaregiverAssignedEvent;
import at.fhv.model.Term;
import at.fhv.repository.TermRepository;
import at.fhv.service.TermCommandService;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@MicronautTest(transactional = false)
class TermCommandServiceTest {

    @Inject
    TermCommandService termCommandService;

    @Inject
    TermRepository termRepository;

    @Inject
    TermEventProducer termEventProducer;

    @MockBean(TermRepository.class)
    TermRepository mockTermRepository() {
        return Mockito.mock(TermRepository.class);
    }

    @MockBean(TermEventProducer.class)
    TermEventProducer mockTermEventProducer() {
        return Mockito.mock(TermEventProducer.class);
    }

    private Term createTermWithId(String id, List<String> caregivers) {
        Term term = new Term();
        try {
            Field field = Term.class.getDeclaredField("termId");
            field.setAccessible(true);
            field.set(term, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set termId via reflection", e);
        }
        term.setCaregiverIds(caregivers);
        return term;
    }

    @BeforeEach
    void setup() {
        Mockito.reset(termRepository, termEventProducer);
    }

    @Test
    void testAssignCaregiver_success() {
        Term term = createTermWithId("123", new ArrayList<>());
        Mockito.when(termRepository.findById("123")).thenReturn(Optional.of(term));
        Mockito.when(termRepository.update(Mockito.any())).thenReturn(term);

        termCommandService.assignCaregiver("123", "caregiver-1");

        Assertions.assertTrue(term.getCaregiverIds().contains("caregiver-1"));
        Mockito.verify(termEventProducer).sendCaregiverAssigned("123", new CaregiverAssignedEvent("123", "caregiver-1"));
    }

    @Test
    void testAssignCaregiver_termNotFound() {
        Mockito.when(termRepository.findById("999")).thenReturn(Optional.empty());

        Assertions.assertThrows(
                TermNotFoundException.class,
                () -> termCommandService.assignCaregiver("999", "caregiver-x"));
    }

    @Test
    void testAssignCaregiver_duplicateCaregiver() {
        Term term = createTermWithId("abc", new ArrayList<>(List.of("caregiver-1")));
        Mockito.when(termRepository.findById("abc")).thenReturn(Optional.of(term));

        Assertions.assertThrows(
                DuplicateAssignmentException.class,
                () -> termCommandService.assignCaregiver("abc", "caregiver-1"));
    }

    @Test
    void testAssignCaregiver_kafkaException_isHandled() {
        Term term = createTermWithId("456", new ArrayList<>());
        Mockito.when(termRepository.findById("456")).thenReturn(Optional.of(term));
        Mockito.when(termRepository.update(Mockito.any())).thenReturn(term);

        Mockito.doThrow(new RuntimeException("Kafka timeout"))
                .when(termEventProducer).sendCaregiverAssigned(Mockito.anyString(), Mockito.any());

        Assertions.assertThrows(RuntimeException.class, () ->
                termCommandService.assignCaregiver("456", "caregiver-9"));
    }
}
