package org.ngcvfb.eventhubkz.Utils;

import org.ngcvfb.eventhubkz.DTO.EventDTO;
import org.ngcvfb.eventhubkz.DTO.EventRequestDTO;
import org.ngcvfb.eventhubkz.DTO.UserDTO;
import org.ngcvfb.eventhubkz.Models.*;
import org.ngcvfb.eventhubkz.Services.EventLikeService;
import org.ngcvfb.eventhubkz.Services.TagService;
import org.ngcvfb.eventhubkz.Services.UserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MappingUtils {
    private final UserService userService;
    private final TagService tagService;
    public MappingUtils(UserService userService, TagService tagService) {
        this.userService = userService;

        this.tagService = tagService;

    }

    public EventDTO mapToEventDTO(EventModel eventModel) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(eventModel.getId());
        eventDTO.setFullDescription(eventModel.getFullDescription());
        eventDTO.setOnline(eventModel.isOnline());
        eventDTO.setLocation(eventModel.getLocation());
        eventDTO.setEventDate(eventModel.getEventDate());
        eventDTO.setOrganizerEmail(eventModel.getOrganizer().getEmail());
        eventDTO.setExternalLink(eventModel.getExternalLink());
        eventDTO.setTags(eventModel.getTags() != null
                ? eventModel.getTags().stream().map(Tag::getName).collect(Collectors.toSet())
                : Collections.emptySet());
        eventDTO.setRegistrationDeadline(eventModel.getRegistrationDeadline());
        eventDTO.setTitle(eventModel.getTitle());
        eventDTO.setMainImageUrl(eventModel.getMainImageUrl());
        eventDTO.setShortDescription(eventModel.getShortDescription());
        eventDTO.setLikeCount(eventModel.getLikes().size());
        return eventDTO;
    }

    public EventModel updateEventModel(EventDTO eventDTO, EventModel eventModel) {
        if (eventDTO.getId() != null) {
            eventModel.setId(eventDTO.getId());
        }
        if (eventDTO.getFullDescription() != null) {
            eventModel.setFullDescription(eventDTO.getFullDescription());
        }
        if (eventDTO.getLocation() != null) {
            eventModel.setLocation(eventDTO.getLocation());
        }
        if (eventDTO.getEventDate() != null) {
            eventModel.setEventDate(eventDTO.getEventDate());
        }
        if (eventDTO.getTags() != null) {
            Set<String> tags = eventDTO.getTags();
            Set<Tag> finalTags = new HashSet<>();
            for (String tagName : tags) {
                if (tags != null) {
                    finalTags.add(tagService.findTag(tagName));
                }
            }
            eventModel.setTags(finalTags);
        }
        if (eventDTO.getOrganizerEmail() != null) {
            eventModel.setOrganizer(userService.getUserByEmail(eventDTO.getOrganizerEmail()));
        }
        if (eventDTO.getExternalLink() != null) {
            eventModel.setExternalLink(eventDTO.getExternalLink());
        }
        if (eventDTO.getRegistrationDeadline() != null) {
            eventModel.setRegistrationDeadline(eventDTO.getRegistrationDeadline());
        }
        if (eventDTO.getTitle() != null) {
            eventModel.setTitle(eventDTO.getTitle());
        }
        if (eventDTO.getMainImageUrl() != null) {
            eventModel.setMainImageUrl(eventDTO.getMainImageUrl());
        }
        if (eventDTO.getShortDescription() != null) {
            eventModel.setShortDescription(eventDTO.getShortDescription());
        }

        eventModel.setOnline(eventDTO.isOnline());
        return eventModel;
    }

    public EventModel mapToEventModel(EventDTO eventDTO) {
        EventModel eventModel = new EventModel();
        if (eventDTO.getId() != null) {
            eventModel.setId(eventDTO.getId());
        }
        if (eventDTO.getFullDescription() != null) {
            eventModel.setFullDescription(eventDTO.getFullDescription());
        }
        if (eventDTO.getLocation() != null) {
            eventModel.setLocation(eventDTO.getLocation());
        }
        if (eventDTO.getEventDate() != null) {
            eventModel.setEventDate(eventDTO.getEventDate());
        }
        if (eventDTO.getTags() != null) {
            Set<String> tags = eventDTO.getTags();
            Set<Tag> finalTags = new HashSet<>();
            for (String tagName : tags) {
                if (tags != null) {
                    finalTags.add(tagService.findTag(tagName));
                }
            }
            eventModel.setTags(finalTags);
        }
        if (eventDTO.getOrganizerEmail() != null) {
            eventModel.setOrganizer(userService.getUserByEmail(eventDTO.getOrganizerEmail()));
        }
        if (eventDTO.getExternalLink() != null) {
            eventModel.setExternalLink(eventDTO.getExternalLink());
        }
        if (eventDTO.getRegistrationDeadline() != null) {
            eventModel.setRegistrationDeadline(eventDTO.getRegistrationDeadline());
        }
        if (eventDTO.getTitle() != null) {
            eventModel.setTitle(eventDTO.getTitle());
        }
        if (eventDTO.getMainImageUrl() != null) {
            eventModel.setMainImageUrl(eventDTO.getMainImageUrl());
        }
        if (eventDTO.getShortDescription() != null) {
            eventModel.setShortDescription(eventDTO.getShortDescription());
        }

        eventModel.setOnline(eventDTO.isOnline());
        return eventModel;
    }

    public EventRequestDTO mapToEventRequestDTO(EventRequest eventRequest) {
        EventRequestDTO eventRequestDTO = new EventRequestDTO();
        eventRequestDTO.setId(eventRequest.getId());
        eventRequestDTO.setTitle(eventRequest.getTitle());
        eventRequestDTO.setShortDescription(eventRequest.getShortDescription());
        eventRequestDTO.setFullDescription(eventRequest.getFullDescription());

        eventRequestDTO.setTags(tagService.findTags(eventRequest
                .getTags()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList()))
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toSet()));
        eventRequestDTO.setLocation(eventRequest.getLocation());
        eventRequestDTO.setOnline(eventRequest.isOnline());
        eventRequestDTO.setEventDate(eventRequest.getEventDate());
        eventRequestDTO.setRegistrationDeadline(eventRequest.getRegistrationDeadline());
        eventRequestDTO.setMainImageUrl(eventRequest.getMainImageUrl());
        eventRequestDTO.setExternalLink(eventRequest.getExternalLink());
        eventRequestDTO.setRequesterEmail(eventRequest.getRequester().getEmail());
        eventRequestDTO.setStatus(eventRequest.getStatus());
        eventRequestDTO.setAdminComment(eventRequest.getAdminComment());
        return eventRequestDTO;
    }

    public EventRequest mapToEventRequest(EventRequestDTO eventRequestDTO) {
        EventRequest eventRequest = new EventRequest();
        eventRequest.setId(eventRequestDTO.getId());
        eventRequest.setTitle(eventRequestDTO.getTitle());
        eventRequest.setShortDescription(eventRequestDTO.getShortDescription());
        eventRequest.setFullDescription(eventRequestDTO.getFullDescription());
        eventRequest.setTags(tagService.findTags(eventRequestDTO.getTags().stream().toList()));
        eventRequest.setLocation(eventRequestDTO.getLocation());
        eventRequest.setOnline(eventRequestDTO.isOnline());
        eventRequest.setEventDate(eventRequestDTO.getEventDate());
        eventRequest.setRegistrationDeadline(eventRequestDTO.getRegistrationDeadline());
        eventRequest.setMainImageUrl(eventRequestDTO.getMainImageUrl());
        eventRequest.setExternalLink(eventRequestDTO.getExternalLink());
        eventRequest.setRequester(userService.getUserByEmail(eventRequestDTO.getRequesterEmail()));
        eventRequest.setStatus(eventRequestDTO.getStatus());
        eventRequest.setAdminComment(eventRequestDTO.getAdminComment());
        return eventRequest;
    }

    public EventDTO mapToEventDTO(EventRequestDTO eventRequest) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle(eventRequest.getTitle());
        eventDTO.setShortDescription(eventRequest.getShortDescription());
        eventDTO.setFullDescription(eventRequest.getFullDescription());
        eventDTO.setLocation(eventRequest.getLocation());
        eventDTO.setOnline(eventRequest.isOnline());
        eventDTO.setEventDate(eventRequest.getEventDate());
        eventDTO.setRegistrationDeadline(eventRequest.getRegistrationDeadline());
        eventDTO.setMainImageUrl(eventRequest.getMainImageUrl());
        eventDTO.setExternalLink(eventRequest.getExternalLink());
        eventDTO.setOrganizerEmail(userService.getUserByEmail(eventRequest.getRequesterEmail()).getEmail());
        eventDTO.setTags(eventRequest.getTags());
        return eventDTO;
    }

    public UserModel mapToUserModel(UserDTO userDTO) {
        UserModel userModel = new UserModel();
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty() && userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()
        && userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) {
            userModel.setUsername(userDTO.getUsername());
            userModel.setEmail(userDTO.getEmail());
            userModel.setPassword(userDTO.getPassword());
        }
        userModel.setDescription(userDTO.getDescription());
        userModel.setAvatarUrl(userDTO.getAvatarUrl());
        userModel.setContacts(userDTO.getContacts());
        if (Objects.equals(userDTO.getRole(), "ADMIN")){
            userModel.setRole(Role.ADMIN);
        }
        else if (Objects.equals(userDTO.getRole(), "USER")){
            userModel.setRole(Role.USER);
        }
        return userModel;
    }

    public UserDTO mapToUserDTO(UserModel userModel) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userModel.getUsername());
        userDTO.setEmail(userModel.getEmail());
        userDTO.setPassword(userModel.getPassword());
        if (userModel.getDescription()!=null){
            userDTO.setDescription(userModel.getDescription());
        }
        if (userModel.getAvatarUrl()!=null){
            userDTO.setAvatarUrl(userModel.getAvatarUrl());
        }
        if (userModel.getContacts()!=null){
            userDTO.setContacts(userModel.getContacts());
        }
        if (Objects.equals(userModel.getRole(), "ADMIN")){
            userDTO.setRole("ADMIN");
        }
        else if (Objects.equals(userModel.getRole(), "USER")){
            userDTO.setRole("USER");
        }
        return userDTO;
    }
}
