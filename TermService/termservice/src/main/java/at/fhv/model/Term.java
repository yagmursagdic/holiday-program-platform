package at.fhv.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Serdeable
@Entity
@Table(name = "terms")
public class Term {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String termId;

    @NotNull("eventId must not be null")
    private String eventId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotBlank
    private String location;
    private String meetingPoint;

    @Min(value = 1, message = "minParticipants must be at least 1")
    private int minParticipants;
    private int maxParticipants;

    @DecimalMin(value =  "0.0")
    private double price;

    // need to know if caregiverIds are String or UUID
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> caregiverIds;

    public Term() {
        //Standard-Constructor for JPA
    }

    public Term(String eventId, LocalDateTime startTime, LocalDateTime endTime,
                String location, String meetingPoint, int minParticipants,
                int maxParticipants, double price, List<String> caregiverIds) {
        this.eventId = eventId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.meetingPoint = meetingPoint;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.price = price;
        this.caregiverIds = caregiverIds;
    }

    // Getter
    public String getTermId() {
        return termId;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getMeetingPoint() {
        return meetingPoint;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getCaregiverIds() {
        return caregiverIds;
    }

    // Setter
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCaregiverIds(List<String> caregiverIds) {
        this.caregiverIds = caregiverIds;
    }

    // equals() und hashCode() based on id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Term t)) return false;
        return Objects.equals(termId, t.termId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(termId);
    }

    // toString()
    @Override
    public String toString() {
        return "Term{" +
                "termId=" + termId +
                ", eventId='" + eventId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                ", meetingPoint='" + meetingPoint + '\'' +
                ", minParticipants=" + minParticipants +
                ", maxParticipants=" + maxParticipants +
                ", price=" + price +
                ", caregiverIds=" + caregiverIds +
                '}';
    }
}
