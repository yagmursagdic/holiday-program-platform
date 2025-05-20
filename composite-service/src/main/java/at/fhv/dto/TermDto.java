// src/main/java/at/fhv/dto/Term.java
package at.fhv.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TermDto {
    public String termId;
    public String eventId;
    public LocalDate date;
    public LocalTime time;
    public String location;
    public String meetingPoint;
    public int minParticipants;
    public int maxParticipants;
    public double price;
    public List<String> caregivers;
}
