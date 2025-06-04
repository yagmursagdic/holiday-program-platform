package at.fhv.exception;

public class TermNotFoundException extends RuntimeException {
    public TermNotFoundException(String termId) {
        super("Term with ID '" + termId + "' not found.");
    }
}