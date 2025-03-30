package org.ngcvfb.eventhubkz.Services;

import org.ngcvfb.eventhubkz.DTO.EventRequestDTO;
import org.ngcvfb.eventhubkz.Models.EventRequest;
import org.ngcvfb.eventhubkz.Models.RequestStatus;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Repository.EventRequestRepository;
import org.ngcvfb.eventhubkz.Utils.MappingUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventRequestService {
    private final EventRequestRepository eventRequestRepository;
    private final UserService userService;
    private final MappingUtils mappingUtils;
    public EventRequestService (EventRequestRepository eventRequestRepository, UserService userService, MappingUtils mappingUtils) {
        this.eventRequestRepository = eventRequestRepository;
        this.userService = userService;
        this.mappingUtils = mappingUtils;
    }

    public EventRequestDTO createEventRequest(EventRequestDTO eventRequestDTO) {
        UserModel userModel = userService.getUserByEmail(eventRequestDTO.getRequesterEmail());
        EventRequest eventRequest = mappingUtils.mapToEventRequest(eventRequestDTO);
        eventRequest.setRequester(userModel);
        eventRequest.setStatus(RequestStatus.PENDING);
        EventRequest savedEventRequest = eventRequestRepository.save(eventRequest);
        return mappingUtils.mapToEventRequestDTO(savedEventRequest);
    }

    public List<EventRequestDTO> getAllEventRequests() {
        return eventRequestRepository.findAll()
                .stream()
                .map(mappingUtils::mapToEventRequestDTO)
                .collect(Collectors.toList());
    }

    public EventRequestDTO getEventRequestById(long id) {
        EventRequest eventRequest = eventRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("No event request found with id: " + id));
        return mappingUtils.mapToEventRequestDTO(eventRequest);

    }

    public void deleteEventRequest(Long id) {
        eventRequestRepository.deleteById(id);
    }

    public EventRequestDTO updateEventRequest(Long id, String status, String adminComment) {
        EventRequest existingRequest = eventRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EventRequest not found"));

        existingRequest.setStatus(Objects.equals(status, "APPROVED") ? RequestStatus.APPROVED : RequestStatus.REJECTED);
        existingRequest.setAdminComment(adminComment);

        EventRequest updatedRequest = eventRequestRepository.save(existingRequest);
        return mappingUtils.mapToEventRequestDTO(updatedRequest);
    }
}
