package org.ngcvfb.eventhubkz.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ngcvfb.eventhubkz.Models.EventDocument;
import org.ngcvfb.eventhubkz.Services.EventSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Search", description = "ElasticSearch search API ")
@RestController
@RequestMapping("/api/search")
public class EventSearchController {
    private final EventSearchService eventSearchService;

    public EventSearchController(EventSearchService eventSearchService) {
        this.eventSearchService = eventSearchService;
    }

    @Operation(summary = "Searching system using elasticsearch")
    @GetMapping
    public ResponseEntity<List<EventDocument>> search(@RequestParam("query") String query) {
        List<EventDocument> results = eventSearchService.searchEvents(query);
        return ResponseEntity.ok(results);
    }

}
