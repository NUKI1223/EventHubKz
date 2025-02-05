package org.ngcvfb.kagglekz.Controllers;


import org.ngcvfb.kagglekz.DTO.EventDTO;
import org.ngcvfb.kagglekz.DTO.EventRequestDTO;
import org.ngcvfb.kagglekz.Models.EventModel;
import org.ngcvfb.kagglekz.Models.RequestStatus;
import org.ngcvfb.kagglekz.Services.EventRequestService;
import org.ngcvfb.kagglekz.Services.EventService;
import org.ngcvfb.kagglekz.Utils.MappingUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final EventRequestService eventRequestService;
    private final EventService eventService;
    private final MappingUtils mappingUtils;
    public AdminController(EventRequestService eventRequestService, EventService eventService, MappingUtils mappingUtils) {
        this.eventRequestService = eventRequestService;
        this.eventService = eventService;
        this.mappingUtils = mappingUtils;
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventRequestDTO>> getAllEventRequests() {
        List<EventRequestDTO> requests = eventRequestService.getAllEventRequests();
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<EventRequestDTO> updateEventRequest(
            @PathVariable Long id,
            @RequestBody EventRequestDTO eventRequestDTO) {
        EventRequestDTO updatedRequest = eventRequestService.updateEventRequest(id, eventRequestDTO);
        if (updatedRequest.getStatus()== RequestStatus.APPROVED){
            EventDTO eventDTO = mappingUtils.mapToEventDTO(updatedRequest);
            eventService.createEvent(eventDTO);
            return ResponseEntity.ok(updatedRequest);
        }
        else if (updatedRequest.getStatus()== RequestStatus.REJECTED){
            eventRequestService.deleteEventRequest(updatedRequest.getId());
            return ResponseEntity.ok(updatedRequest);
        }
        return ResponseEntity.ok(updatedRequest);
    }
}
