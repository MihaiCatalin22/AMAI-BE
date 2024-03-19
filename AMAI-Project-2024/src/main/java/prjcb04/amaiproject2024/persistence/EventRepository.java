package prjcb04.amaiproject2024.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import prjcb04.amaiproject2024.domain.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
