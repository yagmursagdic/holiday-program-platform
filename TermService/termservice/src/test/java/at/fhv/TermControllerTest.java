package at.fhv;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import at.fhv.controller.TermController;
import at.fhv.exception.TermNotFoundException;
import at.fhv.messaging.TermEventProducer;
import at.fhv.messaging.event.CaregiverAssignedEvent;
import at.fhv.model.Term;
import at.fhv.repository.TermRepository;
import at.fhv.service.TermCommandService;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class TermControllerTest {

    @Inject
    TermRepository termRepository;

    @Inject
    TermCommandService termCommandService;

    @Inject
    TermController termController;

    @MockBean(TermRepository.class)
    TermRepository mockTermRepository() {
        return Mockito.mock(TermRepository.class);
    }

    @MockBean(TermEventProducer.class)
    TermEventProducer kafkaProducer() {
        return Mockito.mock(TermEventProducer.class);
    }

    @Inject
    TermEventProducer kafkaProducer;

    @BeforeEach
    void setup() {
        Mockito.reset(termRepository);
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

    @Test
    public void testAssignCaregiver_callsKafkaProducer() {
        Term term = createTermWithId("abc", new ArrayList<>());
        Mockito.when(termRepository.findById("abc")).thenReturn(Optional.of(term));

        termController.assignCaregiver("abc", "caregiver-1");

        Mockito.verify(kafkaProducer).sendCaregiverAssigned("abc", new CaregiverAssignedEvent("abc", "caregiver-1"));
        ;
    }

    @Test
    public void testAssignCaregiver_kafkaException_handling() {
        Term term = createTermWithId("123", new ArrayList<>());
        Mockito.when(termRepository.findById("123")).thenReturn(Optional.of(term));
        Mockito.when(termRepository.update(Mockito.any())).thenReturn(term);

        Mockito.doThrow(new RuntimeException("Kafka Timeout"))
                .when(kafkaProducer).sendCaregiverAssigned(Mockito.anyString(), Mockito.any());

        Assertions.assertDoesNotThrow(() -> termCommandService.assignCaregiver("123", "caregiver-1"));
        Assertions.assertTrue(term.getCaregiverIds().contains("caregiver-1"));
    }

    @Test
    public void testAssignCaregiver_termNotFound_throwsException() {
        Mockito.when(termRepository.findById("999")).thenReturn(Optional.empty());

        Assertions.assertThrows(TermNotFoundException.class,
                () -> termController.assignCaregiver("999", "caregiver-1"));
    }

}
