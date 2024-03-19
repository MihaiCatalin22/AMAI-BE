package prjcb04.amaiproject2024.controller;

import prjcb04.amaiproject2024.domain.Presentation;
import prjcb04.amaiproject2024.business.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/presentations")
public class PresentationController {
    private final PresentationService presentationService;

    @Autowired
    public PresentationController(PresentationService presentationService) {
        this.presentationService = presentationService;
    }

    @PostMapping
    public ResponseEntity<Presentation> createPresentation(@RequestBody Presentation presentation) {
        Presentation newPresentation = presentationService.createPresentation(presentation);
        return ResponseEntity.ok(newPresentation);
    }

    @GetMapping
    public ResponseEntity<List<Presentation>> getAllPresentations() {
        List<Presentation> presentations = presentationService.getAllPresentations();
        return ResponseEntity.ok(presentations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Presentation> getPresentationById(@PathVariable Long id) {
        return presentationService.getPresentationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Presentation> updatePresentation(@PathVariable Long id, @RequestBody Presentation presentation) {
        try {
            Presentation updatedPresentation = presentationService.updatePresentation(id, presentation);
            return ResponseEntity.ok(updatedPresentation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePresentation(@PathVariable Long id) {
        try {
            presentationService.deletePresentation(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
