package at.fhv.service;

import java.util.List;
import java.util.Optional;

import at.fhv.model.Payment;
import at.fhv.model.SponsorPayment;
import at.fhv.repository.PaymentRepository;
import at.fhv.repository.SponsorPaymentRepository;
import jakarta.inject.Singleton;

@Singleton
public class AccountingQueryService {

  private PaymentRepository paymentRepository;
  private SponsorPaymentRepository sponsorPaymentRepository;

  public AccountingQueryService(PaymentRepository paymentRepository,
      SponsorPaymentRepository sponsorPaymentRepository) {
    this.paymentRepository = paymentRepository;
    this.sponsorPaymentRepository = sponsorPaymentRepository;
  }
  
  public Optional<Payment> getPaymentById(String id) {
    return paymentRepository.findById(id);
  }

  public List<Payment> getAllPaymentsByTermId(String termtId) {
    return paymentRepository.findByTermId(termtId);
  }
  
  public Optional<SponsorPayment> getSponsorPaymentById(String id) {
    return sponsorPaymentRepository.findById(id);
  }

  public List<SponsorPayment> getAllSponsorPaymentsBySponsorId(String sponsorId) {
    return sponsorPaymentRepository.findBySponsorId(sponsorId);
  }
  
}
