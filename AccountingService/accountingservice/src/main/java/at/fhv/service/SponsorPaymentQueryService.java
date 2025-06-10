package at.fhv.service;

import java.util.List;
import java.util.Optional;

import at.fhv.model.Payment;
import at.fhv.model.SponsorPayment;
import at.fhv.repository.UserPaymentRepository;
import at.fhv.repository.SponsorPaymentRepository;
import jakarta.inject.Singleton;

@Singleton
public class SponsorPaymentQueryService {

  private final UserPaymentRepository userPaymentRepository;
  private final SponsorPaymentRepository sponsorPaymentRepository;

  public SponsorPaymentQueryService(UserPaymentRepository userPaymentRepository,
                                    SponsorPaymentRepository sponsorPaymentRepository) {
    this.userPaymentRepository = userPaymentRepository;
    this.sponsorPaymentRepository = sponsorPaymentRepository;
  }
  
  public Optional<Payment> getPaymentById(String id) {
    return userPaymentRepository.findById(id);
  }

  public List<Payment> getAllPaymentsByTermId(String termtId) {
    return userPaymentRepository.findByTermId(termtId);
  }
  
  public Optional<SponsorPayment> getSponsorPaymentById(String id) {
    return sponsorPaymentRepository.findById(id);
  }

  public List<SponsorPayment> getAllSponsorPaymentsBySponsorId(String sponsorId) {
    return sponsorPaymentRepository.findBySponsorId(sponsorId);
  }
  
}
