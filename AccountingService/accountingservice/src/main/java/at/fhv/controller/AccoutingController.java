package at.fhv.controller;

import at.fhv.model.Expense;
import at.fhv.model.PaymentRecord;
import at.fhv.model.SponsorPayment;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller("/accounting")
public class AccoutingController {

    @Get("/hello")
    public String hello() {
        return "Hello from AccountingService";
    }

    // 1. Get all payments for a term
    @Get("/payments/{termId}")
    public List<PaymentRecord> getPayments(Long termId) {
        return List.of(
                new PaymentRecord(termId, 1L, false),
                new PaymentRecord(termId, 2L, true)
        );
    }

    // 2. Get open payments for a term
    @Get("/payments/open/term/{termId}")
    public List<PaymentRecord> getOpenPaymentsByTerm(Long termId) {
        return List.of(
                new PaymentRecord(termId, 1L, false)
        );
    }

    // 3. Get open payments for a user
    @Get("/payments/open/user/{userId}")
    public List<PaymentRecord> getOpenPaymentsByUser(Long userId) {
        return List.of(
                new PaymentRecord(100L, userId, false)
        );
    }

    // 4. Create user payment
    @Post("/payment/create")
    public PaymentRecord createUserPayment(@Body Map<String, Object> body) {
        Long termId = ((Number) body.get("termId")).longValue();
        Long userId = ((Number) body.get("userId")).longValue();
        return new PaymentRecord(termId, userId, false);
    }

    // 5. Notify user payment received
    @Post("/payment/notify/received")
    public String notifyUserPaymentReceived(@Body Map<String, Object> body) {
        Long userId = ((Number) body.get("userId")).longValue();
        Long termId = ((Number) body.get("termId")).longValue();
        return "User " + userId + " notified: payment received for term " + termId;
    }

    // 6. Notify user payment pending
    @Post("/payment/notify/pending")
    public String notifyUserPaymentPending(@Body Map<String, Object> body) {
        Long userId = ((Number) body.get("userId")).longValue();
        Long termId = ((Number) body.get("termId")).longValue();
        Double price = ((Number) body.get("price")).doubleValue();
        String paymentDeadline = (String) body.get("paymentDeadline");
        return "User " + userId + " notified: payment pending (" + price + "€) until " + paymentDeadline + " for term " + termId;
    }

    // 7. Get user payment status for a term
    @Get("/payment/status/{userId}/{termId}")
    public List<PaymentRecord> getUserPaymentStatusForTerm(Long userId, Long termId) {
        return List.of(
                new PaymentRecord(termId, userId, true)
        );
    }

    // 8. Mark payment as paid
    @Post("/payment/markPaid/{paymentId}")
    public String markAsPaid(Long paymentId) {
        return "Payment " + paymentId + " marked as paid";
    }

    // 9. Get sponsor payments
    @Get("/sponsor/payments")
    public List<SponsorPayment> getSponsorPayments() {
        List<SponsorPayment> sponsorPayments = new ArrayList<>();
        SponsorPayment sponsorPayment = new SponsorPayment(1L, 500.0, false);
        sponsorPayments.add(sponsorPayment);
        return sponsorPayments;
    }

    // 10. Create sponsor payment
    @Post("/sponsor/payment/create")
    public SponsorPayment createSponsorPayment(@Body Map<String, Object> body) {
        Long sponsorId = ((Number) body.get("sponsorId")).longValue();
        Double amount = ((Number) body.get("amount")).doubleValue();
        return new SponsorPayment(sponsorId, amount, false);
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
