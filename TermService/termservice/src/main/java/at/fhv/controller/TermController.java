package at.fhv.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/term")
public class TermController {

    @Get("/hello")
    public String hello() {
        return "Hello from TermService";
    }
    
}
