package prjcb04.amaiproject2024.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.util.Set;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "speakers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Speaker extends User {

    private String bio;

    private String expertiseArea;

    @OneToMany(mappedBy = "speaker")
    private Set<Presentation> presentations;
}
