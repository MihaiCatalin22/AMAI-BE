package prjcb04.amaiproject2024.business.Implementation;

import prjcb04.amaiproject2024.domain.Event;
import prjcb04.amaiproject2024.domain.User;
import prjcb04.amaiproject2024.persistence.EventRepository;
import prjcb04.amaiproject2024.business.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prjcb04.amaiproject2024.persistence.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EmailSender emailSender, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event updateEvent(Long id, Event eventDetails) {
        return eventRepository.findById(id).map(existingEvent -> {
            existingEvent.setTopic(eventDetails.getTopic());
            existingEvent.setDescription(eventDetails.getDescription());
            existingEvent.setDate(eventDetails.getDate());
            existingEvent.setDuration(eventDetails.getDuration());
            return eventRepository.save(existingEvent);
        }).orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public List<Event> searchEventsBySpeakerFullName(String fullName) {
        Optional<User> user = userRepository.findByFullName(fullName);
        if (user.isPresent()) {
            return eventRepository.findBySpeakerId(user.get().getId());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Event> searchEventsByTopic(String topic) {
        return eventRepository.findByTopicContainingIgnoreCase(topic);
    }

    @Override
    public List<Event> getEventsByDate(LocalDate date) {
        LocalDateTime startOfTimeSlot = date.atTime(16, 0);
        LocalDateTime endOfTimeSlot = date.atTime(17, 0);

        return eventRepository.findByDateTimeSlot(startOfTimeSlot, endOfTimeSlot);
    }

    @Override
    public List<LocalDateTime> getAvailableSlots(LocalDate date, int duration) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        List<Event> events = getEventsByDate(date);

        LocalDateTime slotStart = date.atTime(16, 0);
        LocalDateTime slotEnd = date.atTime(17, 0);

        LocalDateTime firstSlotStart;
        LocalDateTime firstSlotEnd;
        LocalDateTime secondSlotStart;
        LocalDateTime secondSlotEnd;

        // Check the selected duration and adjust time slots accordingly
        if (duration == 10) {
            firstSlotStart = date.atTime(16, 0);
            firstSlotEnd = date.atTime(16, 10);
            secondSlotStart = date.atTime(16, 10);
            secondSlotEnd = date.atTime(16, 20);
        } else if (duration == 20) {
            firstSlotStart = date.atTime(16, 0);
            firstSlotEnd = date.atTime(16, 20);
            secondSlotStart = date.atTime(16, 20);
            secondSlotEnd = date.atTime(16, 40);
        } else {
            firstSlotStart = date.atTime(16, 0);
            firstSlotEnd = date.atTime(16, 30);
            secondSlotStart = date.atTime(16, 30);
            secondSlotEnd = date.atTime(17, 0);
        }

        // Check if slots are available
        boolean firstSlotAvailable = isSlotAvailable(events, firstSlotStart, firstSlotEnd);
        if (firstSlotAvailable) {
            availableSlots.add(firstSlotStart);
        }

        boolean secondSlotAvailable = isSlotAvailable(events, secondSlotStart, secondSlotEnd);
        if (secondSlotAvailable) {
            availableSlots.add(secondSlotStart);
        }
        return availableSlots;

    }

    private boolean isSlotAvailable(List<Event> events, LocalDateTime slotStart, LocalDateTime slotEnd) {
        return events.stream().noneMatch(event ->
                event.getDate().isEqual(slotStart) ||
                        (event.getDate().isAfter(slotStart) &&
                                event.getDate().isBefore(slotEnd))
        );
    }

    @Override
    public void attachFileToEvent(Long eventId, String fileName) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        event.setFileName(fileName);
        eventRepository.save(event);
    }

    @Override
    public void updateEventPresentationFile(Long eventId, String fileName) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        event.setFileName(fileName);
        eventRepository.save(event);
    }
}

