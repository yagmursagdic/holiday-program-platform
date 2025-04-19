package at.fhv.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/accounting")
public class AccoutingController {

    @Get("/hello")
    public String hello() {
        return "Hello from AccountingService";
    }
    
}
