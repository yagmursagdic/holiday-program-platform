package at.fhv.exception;

public class SponsorPaymentNotFoundException extends RuntimeException {
    public SponsorPaymentNotFoundException(String sponsorPaymentId) {
      super("Sponsor Payment with ID '" + sponsorPaymentId + "'not found.");
    }
}
