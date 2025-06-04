package at.fhv.exception;

public class DuplicateAssignmentException extends RuntimeException {
    public DuplicateAssignmentException(String caregiverId) {
        super("Caregiver '" + caregiverId + "' is already assigned to this term.");
    }
}
