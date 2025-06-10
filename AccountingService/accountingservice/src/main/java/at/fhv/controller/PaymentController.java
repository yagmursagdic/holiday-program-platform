package at.fhv.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import at.fhv.messaging.AccountingEventProducer;
import at.fhv.messaging.event.*;
import at.fhv.model.Expense;
import at.fhv.model.Payment;
import at.fhv.model.PaymentStatus;
import at.fhv.model.SponsorPayment;
import at.fhv.service.AccountingCommandService;
import at.fhv.service.AccountingQueryService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

@Controller("/accounting/payment")
public class PaymentController {

  @Inject
  AccountingCommandService accountingCommandService;

  @Inject
  AccountingQueryService accountingQueryService;

  @Inject
  AccountingEventProducer producer;

  @Get("/hello")
  public String hello() {
    return "Hello from AccountingService, this is the payments part";
  }

  // Create payment 
  @Post("/create")
  public HttpResponse<?> createPayment(@Body Payment payment) {
    Payment createdPayment = accountingCommandService.createPayment(payment);

    producer.sendPaymentCreated(
        createdPayment.getPaymentId(),
        new PaymentCreatedEvent(
            createdPayment.getPaymentId(),
            createdPayment.getUserId(),
            createdPayment.getTermId(),
            createdPayment.getAmount(),
            createdPayment.getPaymentDate(),
            createdPayment.getPaymentDeadline(),
            createdPayment.getPaymentStatus()));

    return HttpResponse.ok("Payment: " + createdPayment.getPaymentId() + " created and event sent.");
  }

  // 1. Get payment by Id
  @Get("/{paymentId}")
  public HttpResponse<?> getPayment(@PathVariable String paymentId) {
    Optional<Payment> payment = accountingQueryService.getPaymentById(paymentId);

    if (payment.isEmpty()) {
      return HttpResponse.notFound(Map.of("message", "Payment not found"));
    }

    return HttpResponse.ok(Map.of(
        "message", "Payment found",
        "payment", payment.get()));
  }

  // 2. all payments for a term
  @Get("/term/{termId}")
  public HttpResponse<?> getPaymentsByTerm(@PathVariable String termId) {
    List<Payment> payments = accountingQueryService.getAllPaymentsByTermId(termId);

    if (payments.isEmpty()) {
      return HttpResponse.notFound(Map.of("message", "Payments not found"));
    }

    return HttpResponse.ok(Map.of(
        "message", "Payments found",
        "payment", payments));
  }

  // 3. Notify user payment received
  @Post("/notify/received")
  public HttpResponse<?> notifyUserPaymentReceived(@Body Map<String, Object> body) {
    String userId = body.get("userId").toString();
    String termId = body.get("termId").toString();
    double amount = Double.parseDouble(body.get("amount").toString());
    LocalDate receivedAt = LocalDate.now();

    PaymentReceivedEvent event = new PaymentReceivedEvent(userId, termId, amount, receivedAt);
    producer.sendPaymentReceived(userId, event);

    return HttpResponse.ok("User " + userId + " notified: payment received (" + amount + "€) on " + receivedAt + " for term "
        + termId + " and Payment received event sent.");
  }

  // 4. Notify user payment pending
  @Post("/notify/pending")
  public HttpResponse<?> notifyUserPaymentPending(@Body Map<String, Object> body) {
    String userId = body.get("userId").toString();
    String termId = body.get("termId").toString();
    Double price = ((Number) body.get("price")).doubleValue();
    String paymentDeadline = (String) body.get("paymentDeadline");
    return HttpResponse.ok("User " + userId + " notified: payment pending (" + price + "€) until " + paymentDeadline + " for term "
        + termId);
  }

  // 7. Get user payment status for a term
  @Get("/status/{userId}/{termId}")
  public List<String> getUserPaymentStatusForTerm(String userId, String termId) {
    return List.of("PaymentStatus for user " + userId + " and term " + termId);
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
        new Expense(101L, 75.0, false));
  }

  // 13. Mark expense as paid
  @Post("/expense/markPaid/{expenseId}")
  public String markExpenseAsPaid(Long expenseId) {
    return "Expense " + expenseId + " marked as paid";
  }

  /*
  how to check if payment is open?
  // 3. Get open payments for a term
  @Get("/open/term/{termId}")
  public HttpResponse<?> getOpenPaymentsByTerm(String termId) {
      return List.of("OpenPayment1 for term " + termId, "OpenPayment2 for term " + termId);
  }
  
  // 4. Get open payments for a user
  @Get("/open/user/{userId}")
  public List<String> getOpenPaymentsByUser(String userId) {
      return List.of("OpenPayment1 for user " + userId, "OpenPayment2 for user " + userId);
  }
      relplaced old version
      // 5. Create user payment
    @Post("/create")
    public HttpResponse<?> createUserPayment(@Body Map<String, Object> body) {
        String userId = body.get("userId").toString();
        String termId = body.get("termId").toString();
        double amount = Double.parseDouble(body.get("amount").toString());
        LocalDate deadline = LocalDate.parse(body.get("paymentDeadline").toString());
        String paymentId = "pay-" + System.currentTimeMillis();

        PaymentCreatedEvent event = new PaymentCreatedEvent(paymentId, userId, termId, amount, date, deadline, status);
        producer.sendPaymentCreated(userId, event);

        return HttpResponse.ok("Payment created & event sent.");
    }
  */

}
