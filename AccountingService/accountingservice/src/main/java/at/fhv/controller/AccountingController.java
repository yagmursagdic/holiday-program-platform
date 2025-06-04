package at.fhv.controller;

import at.fhv.messaging.AccountingEventProducer;
import at.fhv.messaging.event.PaymentCreatedEvent;
import at.fhv.messaging.event.PaymentDeletedEvent;
import at.fhv.messaging.event.PaymentReceivedEvent;
import at.fhv.messaging.event.PaymentStatusUpdatedEvent;
import at.fhv.model.Expense;
import at.fhv.model.PaymentStatus;
import at.fhv.model.SponsorPayment;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller("/accounting")
public class AccountingController {

    @Inject
    AccountingEventProducer producer;

    @Get("/hello")
    public String hello() {
        return "Hello from AccountingService";
    }

    // 1. Get all payments for a term
    @Get("/payments/{termId}")
    public List<String> getPayments(String termId) {
        return List.of("Payment1 for term " + termId, "Payment2 for term " + termId);
    }

    // 2. Get open payments for a term
    @Get("/payments/open/term/{termId}")
    public List<String> getOpenPaymentsByTerm(String termId) {
        return List.of("OpenPayment1 for term " + termId, "OpenPayment2 for term " + termId);
    }

    // 3. Get open payments for a user
    @Get("/payments/open/user/{userId}")
    public List<String> getOpenPaymentsByUser(Long userId) {
        return List.of("OpenPayment1 for user " + userId, "OpenPayment2 for user " + userId);
    }

    // 4. Create user payment
    @Post("/payment/create")
    public HttpResponse<?> createUserPayment(@Body Map<String, Object> body) {
        String userId = body.get("userId").toString();
        String termId = body.get("termId").toString();
        double amount = Double.parseDouble(body.get("amount").toString());
        LocalDate deadline = LocalDate.parse(body.get("paymentDeadline").toString());
        String paymentId = "pay-" + System.currentTimeMillis();

        PaymentCreatedEvent event = new PaymentCreatedEvent(paymentId, userId, termId, amount, deadline);
        producer.sendPaymentCreated(userId, event);

        return HttpResponse.ok("Payment created & event sent.");
    }

    // 5. Notify user payment received
    @Post("/payment/notify/received")
    public HttpResponse<?> notifyUserPaymentReceived(@Body Map<String, Object> body) {
        String userId = body.get("userId").toString();
        String termId = body.get("termId").toString();
        double amount = Double.parseDouble(body.get("amount").toString());
        LocalDate receivedAt = LocalDate.now();

        PaymentReceivedEvent event = new PaymentReceivedEvent(userId, termId, amount, receivedAt);
        producer.sendPaymentReceived(userId, event);

        return HttpResponse.ok("Payment received event sent.");
    }

    // 6. Notify user payment pending
    @Post("/payment/notify/pending")
    public String notifyUserPaymentPending(@Body Map<String, Object> body) {
        String userId = body.get("userId").toString();
        String termId = body.get("termId").toString();
        Double price = ((Number) body.get("price")).doubleValue();
        String paymentDeadline = (String) body.get("paymentDeadline");
        return "User " + userId + " notified: payment pending (" + price + "€) until " + paymentDeadline + " for term " + termId;
    }

    // 7. Get user payment status for a term
    @Get("/payment/status/{userId}/{termId}")
    public List<String> getUserPaymentStatusForTerm(String userId, String termId) {
        return List.of("PaymentStatus1 for user " + userId + " and term " + termId);
    }

    // 8. Mark payment as paid
    @Post("/payment/markPaid/{paymentId}")
    public HttpResponse<?> markAsPaid(String paymentId, @Body Map<String, Object> body) {
        String userId = body.get("userId").toString();
        String termId = body.get("termId").toString();

        PaymentStatusUpdatedEvent event = new PaymentStatusUpdatedEvent(paymentId, userId, termId, "PAID");
        producer.sendPaymentStatusUpdated(userId, event);

        return HttpResponse.ok("Payment marked as paid & event sent.");
    }

    @Delete("/payment/delete/{paymentId}")
    public HttpResponse<?> deletePayment(String paymentId) {
        PaymentDeletedEvent event = new PaymentDeletedEvent(paymentId);
        producer.sendPaymentDeleted(paymentId, event);

        return HttpResponse.ok("Payment deleted event sent.");
    }

    // 9. Get sponsor payments
    @Get("/sponsor/payments")
    public List<SponsorPayment> getSponsorPayments() {
        List<SponsorPayment> sponsorPayments = new ArrayList<>();
        SponsorPayment sponsorPayment = new SponsorPayment("1", 500.0, PaymentStatus.PENDING);
        sponsorPayments.add(sponsorPayment);
        return sponsorPayments;
    }

    // 10. Create sponsor payment
    @Post("/sponsor/payment/create")
    public SponsorPayment createSponsorPayment(@Body Map<String, Object> body) {
        String sponsorId = body.get("sponsorId").toString();
        Double amount = ((Number) body.get("amount")).doubleValue();
        return new SponsorPayment(sponsorId, amount, PaymentStatus.PENDING);
    }

    // 11. Create expense
    @Post("/expense/create")
    public Expense createExpense(@Body Map<String, Object> body) {
        Long termId = ((Number) body.get("termId")).longValue();
        Double amount = ((Number) body.get("amount")).doubleValue();
        return new Expense(termId, amount, false);
    }

    // 12. Get all expenses
    @Get("/expenses")
    public List<Expense> getAllExpenses() {
        return List.of(
                new Expense(100L, 150.0, true),
                new Expense(101L, 75.0, false)
        );
    }

    // 13. Mark expense as paid
    @Post("/expense/markPaid/{expenseId}")
    public String markExpenseAsPaid(Long expenseId) {
        return "Expense " + expenseId + " marked as paid";
    }
    
}
