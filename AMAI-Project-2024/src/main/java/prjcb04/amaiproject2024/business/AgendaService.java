package prjcb04.amaiproject2024.business;

import prjcb04.amaiproject2024.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendaService {
    List<Event> getUpcomingEvents(LocalDateTime now);
    List<Event> getPastEvents(LocalDateTime now);
}
