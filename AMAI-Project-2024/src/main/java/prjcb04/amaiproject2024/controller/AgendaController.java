package prjcb04.amaiproject2024.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import prjcb04.amaiproject2024.business.UserService;
import prjcb04.amaiproject2024.domain.Event;
import prjcb04.amaiproject2024.business.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prjcb04.amaiproject2024.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agendas")
public class AgendaController {

    private final AgendaService agendaService;
    private final UserService userService;

    @Autowired
    public AgendaController(AgendaService agendaService, UserService uService) {
        this.agendaService = agendaService;
        this.userService = uService;
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents(@RequestParam(required = false) Integer duration) {
        LocalDateTime now = LocalDateTime.now();
        List<Event> upcomingEvents = agendaService.getUpcomingEvents(now, duration);
        return ResponseEntity.ok(upcomingEvents);
    }

    @GetMapping("/past")
    public ResponseEntity<List<Event>> getPastEvents(@RequestParam(required = false) Integer duration) {
        LocalDateTime now = LocalDateTime.now();
        List<Event> pastEvents = agendaService.getPastEvents(now, duration);
        return ResponseEntity.ok(pastEvents);
    }

    @GetMapping("/upcoming/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('SPEAKER', 'ADMIN')")
    public ResponseEntity<List<Event>> getUpcomingEventsByUser(@PathVariable(value = "userId") final Long userId) {
        LocalDateTime now = LocalDateTime.now();
        if(userService.getUserById(userId) == null){
            return  ResponseEntity.notFound().build();
        }
        User user = userService.getUserById(userId);
        List<Event> upcomingEvents = agendaService.getUpcomingEventsByUser(user, now);
        return ResponseEntity.ok(upcomingEvents);
    }

    @GetMapping("/past/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('SPEAKER', 'ADMIN')")
    public ResponseEntity<List<Event>> getPastEventsByUser(@PathVariable(value = "userId") final Long userId) {
        LocalDateTime now = LocalDateTime.now();
        if(userService.getUserById(userId) == null){
            return  ResponseEntity.notFound().build();
        }
        User user = userService.getUserById(userId);
        List<Event> pastEvents = agendaService.getPastEventsByUser(user, now);
        return ResponseEntity.ok(pastEvents);
    }
}
