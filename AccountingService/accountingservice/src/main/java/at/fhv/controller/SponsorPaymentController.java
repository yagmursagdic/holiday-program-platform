package at.fhv.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;


import at.fhv.model.SponsorPayment;
import at.fhv.service.SponsorPaymentCommandService;
import at.fhv.service.SponsorPaymentQueryService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/accounting/sponsor-payments")
public class SponsorPaymentController {

    private static final Logger LOG = LoggerFactory.getLogger(SponsorPaymentController.class);
    private final SponsorPaymentCommandService sponsorPaymentCommandService;
    private final SponsorPaymentQueryService sponsorPaymentQueryService;

    public SponsorPaymentController(SponsorPaymentCommandService sponsorPaymentCommandService, SponsorPaymentQueryService sponsorPaymentQueryService) {
        this.sponsorPaymentCommandService = sponsorPaymentCommandService;
        this.sponsorPaymentQueryService = sponsorPaymentQueryService;
    }

    // Create sponsor payment
    @Post("")
    public HttpResponse<?> createSponsorPayment(@Body SponsorPayment sponsorPayment) {
        try {
            SponsorPayment createdSponsorPayment = sponsorPaymentCommandService.createSponsorPayment(sponsorPayment);
            return HttpResponse.created(createdSponsorPayment);
        } catch (Exception e) {
            LOG.error("Failed to create sponsor payment");
            return HttpResponse.serverError("Could not creaet term.");
        }
    }

    // Get sponsor payments
    @Get("/payments/{sponsorId}")
    public HttpResponse<?> getSponsorPayments(@PathVariable String sponsorId) {
        try {
            List<SponsorPayment> sponsorPayments = sponsorPaymentQueryService.getAllSponsorPaymentsBySponsorId(sponsorId);
            return HttpResponse.ok(Map.of(
                    "message", "Sponsor payments found",
                    "sponsorPayments", sponsorPayments));

        } catch (Exception e) {
            LOG.error("Error retrieving sponsor payments for sponsor ID {}", sponsorId, e);
            return HttpResponse.serverError("Error retrieving payments.");
        }
    }

    @Put("/{sponsorPaymentId}")
    public HttpResponse<?> updateTerm(@PathVariable String sponsorPaymentId, @Body SponsorPayment sponsorPayment) {
        try {
            Optional<SponsorPayment> updated = sponsorPaymentCommandService.updateSponsorPayment(sponsorPaymentId, sponsorPayment);
            return updated
                    .map(p -> HttpResponse.ok(Map.of(
                            "message", "Sponsor payment updated",
                            "sponsorPayment", p)))
                    .orElseGet(() -> HttpResponse.notFound(Map.of(
                            "message", "Sponsor payment not found")));
        } catch (Exception e) {
            LOG.error("Error updating sponsor payment with ID {}", sponsorPaymentId, e);
            return HttpResponse.serverError("Could not update sponsor payment.");
        }
    }

    @Delete("/{sponsorPaymentId}")
    public HttpResponse<?> deleteTerm(@PathVariable String sponsorPaymentId) {
        try {
            sponsorPaymentCommandService.deleteSponsorPayment(sponsorPaymentId);
            return HttpResponse.ok(Map.of(
                    "message", "Sponsor payment updated",
                    "sponsorPayment", sponsorPaymentId));
        } catch (Exception e) {
            LOG.error("Error deleting sponsor payment with ID {}", sponsorPaymentId, e);
            return HttpResponse.serverError("Could not delete payment.");
        }
    }


}
