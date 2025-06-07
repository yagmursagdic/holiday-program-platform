package at.fhv.service;

import java.util.Optional;

import at.fhv.model.Payment;
import at.fhv.model.PaymentStatus;
import at.fhv.model.SponsorPayment;
import at.fhv.repository.PaymentRepository;
import at.fhv.repository.SponsorPaymentRepository;
import jakarta.inject.Singleton;


@Singleton
public class AccountingCommandService {

  private final PaymentRepository paymentRepository;

  private final SponsorPaymentRepository sponsorPaymentRepository;

  public AccountingCommandService(PaymentRepository paymentRepository,
      SponsorPaymentRepository sponsorPaymentRepository) {
    this.paymentRepository = paymentRepository;
    this.sponsorPaymentRepository = sponsorPaymentRepository;
  }

  public Payment createPayment(Payment payment) {
    return paymentRepository.save(payment);
  }

  public Optional<Payment> updatePayment(String paymentId, Payment updatedPayment) {
    return paymentRepository.findById(paymentId).map(existing -> {
      existing.setPaymentId(updatedPayment.getPaymentId());
      existing.setUserId(updatedPayment.getUserId());
      existing.setTermId(updatedPayment.getTermId());
      existing.setAmount(updatedPayment.getAmount());
      existing.setPaymentDate(updatedPayment.getPaymentDate());
      existing.setPaymentDeadline(updatedPayment.getPaymentDeadline());
      existing.setPaymentStatus(updatedPayment.getPaymentStatus() == PaymentStatus.PAID);
      return paymentRepository.update(existing);
    });
  }

  public void deletePayment(String paymentId) {
    paymentRepository.deleteById(paymentId);
  }
  

  // Sponsor Payment
  public SponsorPayment createSponsorPayment(SponsorPayment sponsorPayment) {
    return sponsorPaymentRepository.save(sponsorPayment);
  }

  public Optional<SponsorPayment> updateSponsorPayment(String sponsorPaymentId, SponsorPayment updatedSponsorPayment) {
    return sponsorPaymentRepository.findById(sponsorPaymentId).map(existing -> {
      existing.setSponsorPaymentId(updatedSponsorPayment.getSponsorPaymentId());
      existing.setSponsorId(updatedSponsorPayment.getSponsorId());
      existing.setOrganizationId(updatedSponsorPayment.getOrganizationId());
      existing.setAmount(updatedSponsorPayment.getAmount());
      existing.setPaymentStatus(updatedSponsorPayment.getPaymentStatus() == PaymentStatus.PAID);
      return sponsorPaymentRepository.update(existing);
    });
  }

  public void deleteSponsorPayment(String sponsorPaymentId) {
    paymentRepository.deleteById(sponsorPaymentId);
  }
}

