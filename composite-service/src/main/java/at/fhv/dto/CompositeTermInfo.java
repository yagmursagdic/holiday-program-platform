// src/main/java/at/fhv/dto/CompositeTermInfo.java
package at.fhv.dto;

import java.util.List;

public class CompositeTermInfo {
    public TermDto term;
    public List<String> payments;

    public CompositeTermInfo(TermDto term, List<String> payments) {
        this.term = term;
        this.payments = payments;
    }
}
