package prjcb04.amaiproject2024.business.Implementation;

import prjcb04.amaiproject2024.business.AgendaService;
import prjcb04.amaiproject2024.domain.Event;
import prjcb04.amaiproject2024.persistence.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendaServiceImpl implements AgendaService {
    private final EventRepository eventRepository;

    @Autowired
    public AgendaServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> getUpcomingEvents(LocalDateTime now) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getDateTime().isAfter(now))
                .collect(Collectors.toList());

    }

    @Override
    public List<Event> getPastEvents(LocalDateTime now) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getDateTime().isBefore(now))
                .collect(Collectors.toList());
    }
}
