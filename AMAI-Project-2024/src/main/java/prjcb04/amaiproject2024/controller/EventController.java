package prjcb04.amaiproject2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prjcb04.amaiproject2024.business.EventService;
import prjcb04.amaiproject2024.domain.Event;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SPEAKER', 'ADMIN')")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasAnyAuthority('SPEAKER', 'ADMIN')")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        try {
            return ResponseEntity.ok(eventService.updateEvent(id, event));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Event>> searchEventsByTopic(String topic) {
        return ResponseEntity.ok(eventService.searchEventsByTopic(topic));
    }
    @GetMapping("/availableSlots")
    public ResponseEntity<List<LocalDateTime>> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam int duration) {
        if (date.getDayOfWeek() != DayOfWeek.TUESDAY) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<LocalDateTime> availableSlots = eventService.getAvailableSlots(date, duration);
        return ResponseEntity.ok(availableSlots);
    }
}
