package prjcb04.amaiproject2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prjcb04.amaiproject2024.business.SpeakerService;
import prjcb04.amaiproject2024.domain.Speaker;

import java.util.List;

@RestController
@RequestMapping("/speakers")
public class SpeakerController {

    private final SpeakerService speakerService;

    @Autowired
    public SpeakerController(SpeakerService speakerService) {
        this.speakerService = speakerService;
    }

    @PostMapping
    public ResponseEntity<Speaker> createSpeaker(@RequestBody Speaker speaker) {
        Speaker newSpeaker = speakerService.registerSpeaker(speaker);
        return ResponseEntity.ok(newSpeaker);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Speaker> getSpeakerById(@PathVariable Long id) {
        Speaker speaker = speakerService.getSpeakerById(id);
        return ResponseEntity.ok(speaker);
    }

    @GetMapping
    public ResponseEntity<List<Speaker>> getAllSpeakers() {
        List<Speaker> speakers = speakerService.getAllSpeakers();
        return ResponseEntity.ok(speakers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Speaker> updateSpeaker(@PathVariable Long id, @RequestBody Speaker speaker) {
        Speaker updatedSpeaker = speakerService.updateSpeakerDetails(id, speaker);
        return ResponseEntity.ok(updatedSpeaker);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpeaker(@PathVariable Long id) {
        speakerService.deleteSpeaker(id);
        return ResponseEntity.ok().build();
    }
}
