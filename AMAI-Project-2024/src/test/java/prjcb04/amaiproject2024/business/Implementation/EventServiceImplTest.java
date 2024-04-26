package prjcb04.amaiproject2024.business.Implementation;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prjcb04.amaiproject2024.domain.Event;
import prjcb04.amaiproject2024.persistence.EventRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setTopic("AI Conference");
        event.setDescription("Annual AI Meetup");
        event.setDate(LocalDateTime.now());
    }

    @Test
    void createEvent_HappyPath() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        Event createdEvent = eventService.createEvent(event);
        assertNotNull(createdEvent);
        verify(eventRepository).save(event);
    }

    @Test
    void getEventById_HappyPath() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        Optional<Event> foundEvent = eventService.getEventById(1L);
        assertTrue(foundEvent.isPresent());
        assertEquals(event.getId(), foundEvent.get().getId());
    }

    @Test
    void getEventById_NotFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<Event> foundEvent = eventService.getEventById(1L);
        assertFalse(foundEvent.isPresent());
    }

    @Test
    void getAllEvents() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));
        List<Event> events = eventService.getAllEvents();
        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
        verify(eventRepository).findAll();
    }

    @Test
    void updateEvent_HappyPath() {
        Event updatedDetails = new Event();
        updatedDetails.setTopic("Updated AI Conference");
        updatedDetails.setDescription("Updated description");
        updatedDetails.setDate(LocalDateTime.now());

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event updatedEvent = eventService.updateEvent(1L, updatedDetails);
        assertNotNull(updatedEvent);
        assertEquals("Updated AI Conference", updatedEvent.getTopic());
        verify(eventRepository).save(event);
    }

    @Test
    void updateEvent_NotFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> eventService.updateEvent(1L, new Event()));
    }

    @Test
    void deleteEvent() {
        doNothing().when(eventRepository).deleteById(anyLong());
        eventService.deleteEvent(1L);
        verify(eventRepository).deleteById(1L);
    }

    @Test
    void searchEventsByTopic() {
        when(eventRepository.findByTopicContainingIgnoreCase("AI")).thenReturn(Arrays.asList(event));
        List<Event> foundEvents = eventService.searchEventsByTopic("AI");
        assertFalse(foundEvents.isEmpty());
        assertEquals(1, foundEvents.size());
        verify(eventRepository).findByTopicContainingIgnoreCase("AI");
    }
}