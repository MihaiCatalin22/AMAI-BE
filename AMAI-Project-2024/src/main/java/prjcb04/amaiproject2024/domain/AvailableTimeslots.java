package prjcb04.amaiproject2024.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AvailableTimeslots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime start;

    @Column(name = "end_time")
    private LocalTime end;

    @Column(name = "is_taken")
    private Boolean isTaken;
}
