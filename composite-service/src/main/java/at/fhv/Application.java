package at.fhv;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}

// to test composition do something like:  curl http://localhost:8083/composite/termWithPayments/term123