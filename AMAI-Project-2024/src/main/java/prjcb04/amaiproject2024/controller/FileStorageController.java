package prjcb04.amaiproject2024.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private FileStorageService fileStorageService;

    private EventService eventService;

    @Autowired
    public FileStorageController(FileStorageService fileStorageService, EventService eventService)
    {
        this.fileStorageService = fileStorageService;
        this.eventService = eventService;
    }
    @PostMapping("/upload/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadFile(@PathVariable Long eventId, @RequestParam("file") MultipartFile file) {
        try {
            System.out.println("Upload request received for event ID: " + eventId);
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize() + " bytes");

            Optional<Event> eventOptional = eventService.getEventById(eventId);
            if (!eventOptional.isPresent()) {
                System.out.println("Event not found with ID: " + eventId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found with id: " + eventId);
            }

            String fileName = fileStorageService.storeFile(file);
            System.out.println("File stored with name: " + fileName);

            eventService.attachFileToEvent(eventId, fileName);
            System.out.println("File attached to event with ID: " + eventId);

            return ResponseEntity.ok().body("File uploaded successfully: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during file upload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the file: " + e.getMessage());
        }
    }


    @GetMapping("/download/{fileName}")
    @PreAuthorize("isAuthenticated()")
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
