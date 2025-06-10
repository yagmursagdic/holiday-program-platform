package at.fhv;

import at.fhv.controller.TermController;
import at.fhv.repository.TermRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class TermControllerTest {

    @Inject
    TermRepository termRepository;

    @Inject
    TermController termController;

    @MockBean(TermRepository.class)
    TermRepository mockTermRepository() {
        return Mockito.mock(TermRepository.class);
    }

    @BeforeEach
    void setup() {
        Mockito.reset(termRepository);
    }

    @Test
    void testAssignCaregiver_termNotFound_returns404() {
        Mockito.when(termRepository.findById("999")).thenReturn(Optional.empty());

        var response = termController.assignCaregiver("999", "caregiver-1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertTrue(response.getBody().isPresent());
        assertEquals("Term not found.", response.getBody().get());
    }
}