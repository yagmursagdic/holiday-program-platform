package at.fhv.client;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import at.fhv.dto.TermDTO;

@Client("http://localhost:8082") // URL of Term Service
public interface TermClient {
    @Get("/term/{termId}")
    TermDto getTerm(String termId);
}