package at.fhv.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import at.fhv.model.Payment;
import at.fhv.service.UserPaymentCommandService;
import at.fhv.service.UserPaymentQueryService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/accounting/user-payments")
public class UserPaymentController {

    private static final Logger LOG = LoggerFactory.getLogger(UserPaymentController.class);
    UserPaymentCommandService userPaymentCommandService;
    UserPaymentQueryService userPaymentQueryService;

    public UserPaymentController(UserPaymentCommandService userPaymentCommandService, UserPaymentQueryService userPaymentQueryService) {
        this.userPaymentCommandService = userPaymentCommandService;
        this.userPaymentQueryService = userPaymentQueryService;
    }

    // Create user payment
    @Post()
    public HttpResponse<?> createUserPayment(@Body Payment payment) {
        try {
            Payment createdPayment = userPaymentCommandService.createUserPayment(payment);
            return HttpResponse.ok(createdPayment);
        } catch (Exception e) {
            LOG.error("Failed to create user payment", e);
            return HttpResponse.serverError("Could not create user payment.");
        }
    }

    // Get payment by Id
    @Get("/{userPaymentId}")
    public HttpResponse<?> getUserPayment(@PathVariable String userPaymentId) {
        try {
            Optional<Payment> payment = userPaymentQueryService.getPaymentById(userPaymentId);
            return payment.map(p -> HttpResponse.ok(Map.of(
                            "message", "Payment found",
                            "payment", p)))
                    .orElseGet(() -> HttpResponse.notFound(Map.of(
                            "message", "Payment not found")));
        } catch (Exception e) {
            LOG.error("Error retrieving payment with ID {}", userPaymentId, e);
            return HttpResponse.serverError("Error retrieving payment.");
        }
    }

    // All payments for a term
    @Get("/by-term/{termId}")
    public HttpResponse<?> getUserPaymentsByTerm(@PathVariable String termId) {
        try {
            List<Payment> payments = userPaymentQueryService.getAllPaymentsByTermId(termId);
            return HttpResponse.ok(Map.of(
                    "message", "Payments found",
                    "payments", payments));
        } catch (Exception e) {
            LOG.error("Error retrieving payments for term ID {}", termId, e);
            return HttpResponse.serverError("Error retrieving payments.");
        }
    }

    // Update payment by ID
    @Put("/{userPaymentId}")
    public HttpResponse<?> updateUserPayment(@PathVariable String userPaymentId, @Body Payment payment) {
        try {
            Optional<Payment> updated = userPaymentCommandService.updateUserPayment(userPaymentId, payment);
            return updated
                    .map(p -> HttpResponse.ok(Map.of(
                            "message", "Payment updated",
                            "payment", p)))
                    .orElseGet(() -> HttpResponse.notFound(Map.of(
                            "message", "Payment not found")));
        } catch (Exception e) {
            LOG.error("Error updating payment with ID {}", userPaymentId, e);
            return HttpResponse.serverError("Could not update payment.");
        }
    }

    // Delete payment by ID
    @Delete("/{userPaymentId}")
    public HttpResponse<?> deleteUserPayment(String userPaymentId) {
        try {
            userPaymentCommandService.deleteUserPayment(userPaymentId);
            return HttpResponse.ok(Map.of(
                    "message", "Payment deleted",
                    "payment", userPaymentId));
        } catch (Exception e) {
            LOG.error("Error deleting payment with ID {}", userPaymentId, e);
            return HttpResponse.serverError("Could not delete payment.");
        }
    }

    // Notify user payment received
    @Post("/notify/received")
    public HttpResponse<?> notifyUserPaymentReceived(@Body Map<String, Object> body) {
        try {
            String userId = body.get("userId").toString();
            String termId = body.get("termId").toString();
            double amount = Double.parseDouble(body.get("amount").toString());

            userPaymentCommandService.notifyPaymentReceived(userId, termId, amount);

            return HttpResponse.ok(Map.of(
                    "message", "User notified: payment received",
                    "userId", userId,
                    "amount", amount,
                    "termId", termId));
        } catch (Exception e) {
            LOG.error("Error notifying payment received", e);
            return HttpResponse.serverError("Could not notify payment received.");
        }
    }

    // Notify user payment pending
    @Post("/notify/pending")
    public HttpResponse<?> notifyUserPaymentPending(@Body Map<String, Object> body) {
        try {
            String userId = body.get("userId").toString();
            String termId = body.get("termId").toString();
            Double price = ((Number) body.get("price")).doubleValue();
            String paymentDeadline = body.get("paymentDeadline").toString();

            userPaymentCommandService.notifyPaymentPending(userId, termId, price, paymentDeadline);

            return HttpResponse.ok(Map.of(
                    "message", "User notified: payment pending",
                    "userId", userId,
                    "price", price,
                    "paymentDeadline", paymentDeadline,
                    "termId", termId));
        } catch (Exception e) {
            LOG.error("Error notifying payment pending", e);
            return HttpResponse.serverError("Could not notify payment pending.");
        }
    }

    // Get user payment status for a term
    @Get("/status/{userId}/{termId}")
    public HttpResponse<?> getUserPaymentStatusForTerm(@PathVariable String userId, @PathVariable String termId) {
        try {
            List<Payment> payments = userPaymentQueryService.getPaymentStatusByTermId(userId, termId);
            return HttpResponse.ok(Map.of(
                    "message", "Payment status found",
                    "userId", userId,
                    "termId", termId,
                    "payments", payments));
        } catch (Exception e) {
            LOG.error("Error retrieving open payments for term ID {}", termId, e);
            return HttpResponse.serverError("Error retrieving payments.");
        }
    }

    // Mark payment as paid
    @Post("/mark-paid/{paymentId}")
    public HttpResponse<?> markUserPaymentAsPaid(@PathVariable String paymentId) {
        try {

            boolean updated = userPaymentCommandService.markUserPaymentAsPaid(paymentId);

            if (!updated) {
                return HttpResponse.notFound(Map.of("message", "Payment not found or could not be updated"));
            }

            return HttpResponse.ok(Map.of(
                    "message", "Payment marked as paid & event sent",
                    "paymentId", paymentId));
        } catch (Exception e) {
            LOG.error("Error marking payment as paid for paymentId {}", paymentId, e);
            return HttpResponse.serverError("Could not mark payment as paid.");
        }
    }


    // Get open payments for a term
    @Get("/open/by-term/{termId}")
    public HttpResponse<?> getOpenPaymentsByTerm(@PathVariable String termId) {
        try {
            List<Payment> payments = userPaymentQueryService.getAllOpenPaymentsByTermId(termId);
            return HttpResponse.ok(Map.of(
                    "message", "Open payments found",
                    "termId", termId,
                    "payments", payments));
        } catch (Exception e) {
            LOG.error("Error retrieving open payments for term ID {}", termId, e);
            return HttpResponse.serverError("Error retrieving payments.");
        }
    }

    // Get open payments for a user
    @Get("/open/{userId}")
    public HttpResponse<?> getOpenPaymentsByUser(@PathVariable String userId) {
        try {
            List<Payment> payments = userPaymentQueryService.getAllOpenPaymentsByUserId(userId);
            return HttpResponse.ok(Map.of(
                    "message", "Open payments found",
                    "userId", userId,
                    "payments", payments));
        } catch (Exception e) {
            LOG.error("Error retrieving open payments for user ID {}", userId, e);
            return HttpResponse.serverError("Error retrieving payments.");
        }
    }

}
