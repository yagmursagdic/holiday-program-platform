package at.fhv.client;

import java.util.List;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("http://localhost:8081") // URL of Accounting service
public interface AccountingClient {
    @Get("/accounting/payments/{termId}")
    List<String> getPayments(String termId);
}
