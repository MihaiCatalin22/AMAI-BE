package prjcb04.amaiproject2024.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import prjcb04.amaiproject2024.domain.Speaker;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
}