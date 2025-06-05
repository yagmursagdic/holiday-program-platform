package at.fhv.service;

import java.util.Optional;

import at.fhv.model.Payment;
import at.fhv.model.SponsorPayment;
import at.fhv.repository.PaymentRepository;
import at.fhv.repository.SponsorPaymentRepository;
import jakarta.inject.Singleton;


@Singleton
public class AccountingCommandService {

  private final PaymentRepository paymentRepository;

  private final SponsorPaymentRepository sponsorPaymentRepository;


  public AccountingCommandService(PaymentRepository paymentRepository, SponsorPaymentRepository sponsorPaymentRepository) {
    this.paymentRepository = paymentRepository;
    this.sponsorPaymentRepository = sponsorPaymentRepository;

  }
  
  public Payment createPayment(Payment payment) {
    return paymentRepository.save(payment);
  } 

  public SponsorPayment createSponsorPayment(SponsorPayment sponsorPayment) {
    return sponsorPaymentRepository.save(sponsorPayment);
  }
  
  public Optional<Payment> updatePayment(String paymentId, Payment updatedPayment) {
        return paymentRepository.findById(paymentId).map(existing -> {
            existing.setPaymentId(updatedPayment.getPaymentId());
            existing.setStartTime(updatedPayment.getStartTime());
            existing.setEndTime(updatedPayment.getEndTime());
            existing.setLocation(updatedPayment.getLocation());
            existing.setMeetingPoint(updatedPayment.getMeetingPoint());
            existing.setMinParticipants(updatedPayment.getMinParticipants());
            existing.setMaxParticipants(updatedPayment.getMaxParticipants());
            existing.setPrice(updatedPayment.getPrice());
            existing.setCaregiverIds(updatedPayment.getCaregiverIds());
            return paymentRepository.update(existing);
        });
    }
}
