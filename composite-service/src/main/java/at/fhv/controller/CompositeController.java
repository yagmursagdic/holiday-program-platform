// src/main/java/at/fhv/controller/CompositeController.java
package at.fhv.controller;

import at.fhv.client.AccountingClient;
import at.fhv.client.TermClient;
import at.fhv.dto.CompositeTermInfo;
import at.fhv.dto.TermDto;
import io.micronaut.http.annotation.*;

import java.util.*;
import java.net.http.HttpClient;
import jakarta.inject.Inject;

@Controller("/composite")
public class CompositeController {

    @Inject
    AccountingClient accountingClient;

    @Inject
    TermClient termClient;

    @Get("/termWithPayments/{termId}")
    public CompositeTermInfo getTermWithPayments(@PathVariable String termId) {
      Term term = termClient.getTerm(termId);
      List<String> payments = accountingClient.getPayments(termId);
      return new CompositeTermInfo(term, payments);
    }
    
    @Get("/term-summary/{termId}")
    public Map<String, Object> getTermSummary(@PathVariable String termId) {
      TermDTO term = termClient.getTerm(termId);
      List<String> payments = accountingClient.getPayments(termId);

      Map<String, Object> summary = new HashMap<>();
      summary.put("term", term);
      summary.put("payments", payments);

      return summary;
  }
}

// to test composition do something like:  curl http://localhost:8083/composite/termWithPayments/term123