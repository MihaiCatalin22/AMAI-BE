package prjcb04.amaiproject2024.business.Implementation;

import prjcb04.amaiproject2024.business.AgendaService;
import prjcb04.amaiproject2024.domain.Event;
import prjcb04.amaiproject2024.persistence.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AgendaServiceImpl implements AgendaService {
    private final EventRepository eventRepository;

    @Autowired
    public AgendaServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> getUpcomingEvents(LocalDateTime now, Integer duration) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getDate().isAfter(now))
                .filter(event -> duration == null || event.getDuration() == (duration))
                .sorted(Comparator.comparing(Event::getDate))
                .toList();
    }

    @Override
    public List<Event> getPastEvents(LocalDateTime now, Integer duration) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getDate().isBefore(now))
                .filter(event -> duration == null || event.getDuration() == (duration))
                .toList();
    }

}
