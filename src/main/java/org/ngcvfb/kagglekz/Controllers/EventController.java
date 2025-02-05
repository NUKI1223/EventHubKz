package org.ngcvfb.kagglekz.Controllers;

import lombok.RequiredArgsConstructor;
import org.ngcvfb.kagglekz.DTO.EventDTO;
import org.ngcvfb.kagglekz.Models.EventModel;
import org.ngcvfb.kagglekz.Models.Tag;
import org.ngcvfb.kagglekz.Services.EventService;
import org.ngcvfb.kagglekz.Services.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        EventDTO event = eventService.getEvent(id);
        return ResponseEntity.ok(event);
    }
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO event) {
        EventDTO created = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
        //TODO: Maybe add check for relevant tag
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> searchByTags(@RequestParam List<String> tags) {
        Set<Tag> tagsSet = new HashSet<>(tagService.findTag(tags));
        List<EventDTO> events = eventService.getEventsByTags(tagsSet);
        return ResponseEntity.ok(events);
        //TODO: Add try catch for error and check for relevant tag
    }
}
