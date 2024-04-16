package prjcb04.amaiproject2024.business.Implementation;


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
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private AgendaServiceImpl agendaService;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
    }

    @Test
    void testGetUpcomingEvents_HappyCase() {
        Event futureEvent1 = Event.builder().topic("Future Event 1").date(now.plusDays(1)).build();
        Event futureEvent2 = Event.builder().topic("Future Event 2").date(now.plusDays(2)).build();
        when(eventRepository.findAll()).thenReturn(Arrays.asList(futureEvent1, futureEvent2));

        List<Event> upcomingEvents = agendaService.getUpcomingEvents(now);

        assertEquals(2, upcomingEvents.size());
        assertTrue(upcomingEvents.contains(futureEvent1));
        assertTrue(upcomingEvents.contains(futureEvent2));
    }

    @Test
    void testGetUpcomingEvents_NoEvents() {
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        List<Event> upcomingEvents = agendaService.getUpcomingEvents(now);

        assertTrue(upcomingEvents.isEmpty());
    }

    @Test
    void testGetPastEvents_HappyCase() {
        Event pastEvent1 = Event.builder().topic("Past Event 1").date(now.minusDays(1)).build();
        Event pastEvent2 = Event.builder().topic("Past Event 2").date(now.minusDays(2)).build();
        when(eventRepository.findAll()).thenReturn(Arrays.asList(pastEvent1, pastEvent2));

        List<Event> pastEvents = agendaService.getPastEvents(now);

        assertEquals(2, pastEvents.size());
        assertTrue(pastEvents.contains(pastEvent1));
        assertTrue(pastEvents.contains(pastEvent2));
    }

    @Test
    void testGetPastEvents_NoEvents() {
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        List<Event> pastEvents = agendaService.getPastEvents(now);

        assertTrue(pastEvents.isEmpty());
    }

    @Test
    void testGetUpcomingEvents_ExactBoundary() {
        Event boundaryEvent = Event.builder().topic("Boundary Event").date(now).build();
        when(eventRepository.findAll()).thenReturn(Arrays.asList(boundaryEvent));

        List<Event> upcomingEvents = agendaService.getUpcomingEvents(now);

        assertFalse(upcomingEvents.contains(boundaryEvent));
    }

    @Test
    void testGetPastEvents_ExactBoundary() {
        Event boundaryEvent = Event.builder().topic("Boundary Event").date(now).build();
        when(eventRepository.findAll()).thenReturn(Arrays.asList(boundaryEvent));

        List<Event> pastEvents = agendaService.getPastEvents(now);

        assertFalse(pastEvents.contains(boundaryEvent));
    }
}