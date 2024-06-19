package prjcb04.amaiproject2024.business;

import prjcb04.amaiproject2024.domain.Event;
import prjcb04.amaiproject2024.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendaService {
    List<Event> getUpcomingEvents(LocalDateTime now, Integer duration);
    List<Event> getPastEvents(LocalDateTime now, Integer duration);

    List<Event> getUpcomingEventsByUser(User user, LocalDateTime now);
    List<Event> getPastEventsByUser(User user, LocalDateTime now);
}
