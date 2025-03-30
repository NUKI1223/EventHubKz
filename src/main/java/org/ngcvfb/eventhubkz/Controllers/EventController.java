package org.ngcvfb.eventhubkz.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ngcvfb.eventhubkz.DTO.EventDTO;
import org.ngcvfb.eventhubkz.Services.EventService;
import org.ngcvfb.eventhubkz.Services.S3Service;
import org.ngcvfb.eventhubkz.Services.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Tag(name = "Event", description = "Event API")
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final S3Service s3Service;
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "Getting event by id")
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        if (eventService.getEvent(id) == null) {
            return ResponseEntity.notFound().build();
        }
        EventDTO event = eventService.getEvent(id);
        return ResponseEntity.ok(event);
    }
    @Operation(summary = "Creating event")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEvent(@RequestPart("event") EventDTO eventDto,
                                         @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                File tempFile = File.createTempFile("event-", file.getOriginalFilename());
                file.transferTo(tempFile);
                String key = "events/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
                String imageUrl = s3Service.uploadFile(key, tempFile);
                tempFile.delete();
                eventDto.setMainImageUrl(imageUrl);
            }
            EventDTO createdEvent = eventService.createEvent(eventDto);
            return ResponseEntity.ok(createdEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }

    }

    @Operation(summary = "Updating event by id")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id,@RequestBody EventDTO event) {
        if (eventService.getEvent(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventService.updateEvent(id, event));
    }

    @Operation(summary = "Deleting event by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventService.getEvent(id) == null) {
            return ResponseEntity.notFound().build();
        }
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
