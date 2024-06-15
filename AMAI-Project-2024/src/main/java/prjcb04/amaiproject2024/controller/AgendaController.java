package prjcb04.amaiproject2024.controller;


import prjcb04.amaiproject2024.domain.Event;
import prjcb04.amaiproject2024.business.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendas")
public class AgendaController {

    private final AgendaService agendaService;

    @Autowired
    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
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
}
