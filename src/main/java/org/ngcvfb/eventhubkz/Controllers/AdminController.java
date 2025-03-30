package org.ngcvfb.eventhubkz.Controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ngcvfb.eventhubkz.DTO.AdminAnswerDto;
import org.ngcvfb.eventhubkz.DTO.EventDTO;
import org.ngcvfb.eventhubkz.DTO.EventRequestDTO;
import org.ngcvfb.eventhubkz.Models.RequestStatus;
import org.ngcvfb.eventhubkz.Models.Role;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Services.EventRequestService;
import org.ngcvfb.eventhubkz.Services.EventService;
import org.ngcvfb.eventhubkz.Utils.MappingUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Admin", description = "Admin api for users with ADMIN role")
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

    @Operation(summary = "Getting all eventRequests")
    @GetMapping("/all")
    public ResponseEntity<?> getAllEventRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel currentUser = (UserModel) authentication.getPrincipal();
        if (currentUser.getRole() != Role.ADMIN){
            return ResponseEntity.status(403).body("You are not an admin");
        }
        List<EventRequestDTO> requests = eventRequestService.getAllEventRequests();
        return ResponseEntity.ok(requests);
    }

    @Operation(summary = "Updating eventRequest(Approve or reject)")
    @PutMapping("/{id}/update")
    public ResponseEntity<EventRequestDTO> updateEventRequest(
            @PathVariable Long id,
            @RequestBody AdminAnswerDto adminAnswerDto) {
        EventRequestDTO updatedRequest = eventRequestService.updateEventRequest(id, adminAnswerDto.getStatus(), adminAnswerDto.getAdminComment());
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
