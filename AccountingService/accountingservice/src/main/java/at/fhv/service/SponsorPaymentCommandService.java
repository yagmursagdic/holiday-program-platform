package at.fhv.service;

import java.util.Optional;

import at.fhv.messaging.AccountingEventProducer;
import at.fhv.messaging.event.SponsorPaymentCreatedEvent;
import at.fhv.messaging.event.SponsorPaymentDeletedEvent;
import at.fhv.messaging.event.SponsorPaymentUpdatedEvent;
import at.fhv.model.Payment;
import at.fhv.model.PaymentStatus;
import at.fhv.model.SponsorPayment;
import at.fhv.repository.SponsorPaymentRepository;
import jakarta.inject.Singleton;


@Singleton
public class SponsorPaymentCommandService {

    private final SponsorPaymentRepository sponsorPaymentRepository;
    private final AccountingEventProducer producer;

    public SponsorPaymentCommandService(SponsorPaymentRepository sponsorPaymentRepository, AccountingEventProducer producer) {
        this.sponsorPaymentRepository = sponsorPaymentRepository;
        this.producer = producer;
    }

    public SponsorPayment createSponsorPayment (SponsorPayment payment) {
        try {
            SponsorPayment created = sponsorPaymentRepository.save(payment);

            producer.sendSponsorPaymentCreated(
                    created.getSponsorPaymentId(),
                    new SponsorPaymentCreatedEvent(
                            created.getSponsorId(),
                            created.getOrganizationId(),
                            created.getAmount()
                    ));

            return created;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create sponsor payment", e);
        }
    }

    public Optional<SponsorPayment> updateSponsorPayment(String sponsorPaymentId, SponsorPayment sponsorPayment) {
        return sponsorPaymentRepository.findById(sponsorPaymentId).map(existing -> {
            existing.setSponsorId(sponsorPayment.getSponsorId());
            existing.setOrganizationId(sponsorPayment.getOrganizationId());
            existing.setAmount(sponsorPayment.getAmount());

            SponsorPayment updated = sponsorPaymentRepository.update(existing);

            producer.sendSponsorPaymentUpdated(updated.getSponsorPaymentId(), new SponsorPaymentUpdatedEvent(
                    updated.getSponsorId(),
                    updated.getOrganizationId(),
                    updated.getPaymentStatus()
            ));

            return  updated;
        });
    }


    public void deleteSponsorPayment(String sponsorPaymentId) {
        try {
            sponsorPaymentRepository.deleteById(sponsorPaymentId);
            producer.sendSponsorPaymentDeleted(sponsorPaymentId, new SponsorPaymentDeletedEvent(sponsorPaymentId));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete sponsor payment with id:" + sponsorPaymentId, e);
        }

    }
}

