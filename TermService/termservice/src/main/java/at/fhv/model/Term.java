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

    public Term() {}

}
