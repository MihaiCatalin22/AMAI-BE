package prjcb04.amaiproject2024.business;

import prjcb04.amaiproject2024.domain.Event;
import java.util.List;
import java.util.Optional;

public interface EventService {
    Event createEvent(Event event);
    Optional<Event> getEventById(Long id);
    List<Event> getAllEvents();
    Event updateEvent(Long id, Event event);
    void deleteEvent(Long id);

    List<Event> searchEventsByTopic(String topic);

}
