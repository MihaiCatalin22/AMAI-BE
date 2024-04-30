package prjcb04.amaiproject2024.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import prjcb04.amaiproject2024.business.EventService;
import prjcb04.amaiproject2024.business.Implementation.FileStorageService;

import prjcb04.amaiproject2024.domain.Event;

import java.util.Optional;


@RestController
@RequestMapping("/files")
public class FileStorageController {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private EventService eventService;

    @PostMapping("/upload/{eventId}")
    public ResponseEntity<?> uploadFile(@PathVariable Long eventId, @RequestParam("file") MultipartFile file) {
        try {
            Optional<Event> eventOptional = eventService.getEventById(eventId);
            if (!eventOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found with id: " + eventId);
            }

            String fileName = fileStorageService.storeFile(file);

            eventService.attachFileToEvent(eventId, fileName);

            return ResponseEntity.ok().body("File uploaded successfully: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the file: " + e.getMessage());
        }
    }


    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Resource resource = fileStorageService.loadFileAsResource(fileName);
            String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}
