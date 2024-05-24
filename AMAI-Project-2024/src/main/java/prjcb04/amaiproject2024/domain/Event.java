package prjcb04.amaiproject2024.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import prjcb04.amaiproject2024.config.dateFormaters.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;
    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "speaker_id")
    private User speaker;

    @ElementCollection
    private List<String> speakers;

//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @Column(name = "file_name")
    private String fileName;
}
