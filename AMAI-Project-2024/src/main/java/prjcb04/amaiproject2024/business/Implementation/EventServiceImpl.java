package prjcb04.amaiproject2024.business.Implementation;

import prjcb04.amaiproject2024.business.EventService;
import prjcb04.amaiproject2024.domain.AvailableTimeslots;
import prjcb04.amaiproject2024.domain.Event;
import prjcb04.amaiproject2024.persistence.AvailableTimeslotsRepo;
import prjcb04.amaiproject2024.persistence.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final AvailableTimeslotsRepo availableTimeslotsRepo;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, AvailableTimeslotsRepo availableTimeslotsRepo) {
        this.eventRepository = eventRepository;
        this.availableTimeslotsRepo = availableTimeslotsRepo;
    }

    @Override
    public Event createEvent(Event event) {
        LocalDate eventDate = event.getDate().toLocalDate();
        LocalDateTime eventStartTime = event.getDate();
        LocalDateTime eventEndTime = event.getDate().plusMinutes(event.getDuration());

        List<AvailableTimeslots> slots = availableTimeslotsRepo.findByDate(eventDate);

        System.out.println("Creating event from " + eventStartTime + " to " + eventEndTime);

        boolean isSlotTaken = slots.stream().anyMatch(slot -> {
            LocalDateTime slotStartTime = LocalDateTime.of(slot.getDate(), slot.getStart());
            LocalDateTime slotEndTime = LocalDateTime.of(slot.getDate(), slot.getEnd());
            System.out.println("Checking timeslot from " + slotStartTime + " to " + slotEndTime);
            return !slot.getIsTaken() &&
                    (eventStartTime.isBefore(slotEndTime) && eventEndTime.isAfter(slotStartTime));
        });

        if (isSlotTaken) {
            throw new IllegalArgumentException("The selected timeslot is already taken.");
        }

        for (AvailableTimeslots slot : slots) {
            LocalDateTime slotStartTime = LocalDateTime.of(slot.getDate(), slot.getStart());
            LocalDateTime slotEndTime = LocalDateTime.of(slot.getDate(), slot.getEnd());

            if ((eventStartTime.isBefore(slotEndTime) && eventEndTime.isAfter(slotStartTime)) ||
                    (eventStartTime.isEqual(slotStartTime) && eventEndTime.isEqual(slotEndTime))) {
                slot.setIsTaken(true);
                availableTimeslotsRepo.save(slot);
                System.out.println("Updated timeslot: " + slot);
            }
        }

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
    public List<AvailableTimeslots> getAvailableSlots(LocalDate date, int duration) {
        List<AvailableTimeslots> slots = availableTimeslotsRepo.findByDate(date);

        return slots.stream()
                .filter(slot -> !slot.getIsTaken())
                .collect(Collectors.toList());
    }

    private boolean isSlotAvailable(List<Event> events, LocalDateTime slotStart, LocalDateTime slotEnd) {
        return events.stream().noneMatch(event ->
                event.getDate().isEqual(slotStart) ||
                        (event.getDate().isAfter(slotStart) && event.getDate().isBefore(slotEnd))
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
