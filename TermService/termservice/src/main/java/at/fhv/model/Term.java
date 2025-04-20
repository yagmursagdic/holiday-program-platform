package at.fhv.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "terms")
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ElementCollection
    private List<String> caregiverNames;

    public Term() {
    }
    

    // getters
    public Long getId() {
      return id;
    }

    public String getEventName() {
      return eventName;
    }

    public LocalDateTime getStartTime() {
      return startTime;
    }

    public LocalDateTime getEndTime() {
      return endTime;
    }

    // setters
    public void setId(Long id) {
      this.id = id;
    }

    public void setEventName(String eventName) {
      this.eventName = eventName;
    }

    public void setStartTime(LocalDateTime startTime) {
      this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
      this.endTime = endTime;
    }

}
