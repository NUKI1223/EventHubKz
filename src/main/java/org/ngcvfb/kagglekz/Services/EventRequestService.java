package org.ngcvfb.kagglekz.Services;

import org.ngcvfb.kagglekz.DTO.EventRequestDTO;
import org.ngcvfb.kagglekz.Models.EventRequest;
import org.ngcvfb.kagglekz.Models.RequestStatus;
import org.ngcvfb.kagglekz.Models.UserModel;
import org.ngcvfb.kagglekz.Repository.EventRequestRepository;
import org.ngcvfb.kagglekz.Utils.MappingUtils;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public EventRequestDTO createEventRequest(EventRequestDTO eventRequestDTO, String recruiterEmail) {
        UserModel userModel = userService.getUserByEmail(recruiterEmail);
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

    public EventRequestDTO updateEventRequest(Long id, EventRequestDTO eventRequestDTO) {
        EventRequest existingRequest = eventRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EventRequest not found"));

        existingRequest.setStatus(eventRequestDTO.getStatus());
        existingRequest.setAdminComment(eventRequestDTO.getAdminComment());

        EventRequest updatedRequest = eventRequestRepository.save(existingRequest);
        return mappingUtils.mapToEventRequestDTO(updatedRequest);
    }
}
