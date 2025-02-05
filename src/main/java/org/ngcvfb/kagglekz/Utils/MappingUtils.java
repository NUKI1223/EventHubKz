package org.ngcvfb.kagglekz.Utils;

import org.ngcvfb.kagglekz.DTO.EventDTO;
import org.ngcvfb.kagglekz.DTO.EventRequestDTO;
import org.ngcvfb.kagglekz.DTO.UserDTO;
import org.ngcvfb.kagglekz.Models.*;
import org.ngcvfb.kagglekz.Services.EventService;
import org.ngcvfb.kagglekz.Services.TagService;
import org.ngcvfb.kagglekz.Services.UserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MappingUtils {
    private final UserService userService;
    private final TagService tagService;
    public MappingUtils(UserService userService,  TagService tagService) {
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
        return eventDTO;
    }

    public EventModel mapToEventModel(EventDTO eventDTO) {
        EventModel eventModel = new EventModel();
        eventModel.setId(eventDTO.getId());
        eventModel.setFullDescription(eventDTO.getFullDescription());
        eventModel.setOnline(eventDTO.isOnline());
        eventModel.setLocation(eventDTO.getLocation());
        eventModel.setEventDate(eventDTO.getEventDate());
        eventModel.setOrganizer(userService.getUserByEmail(eventDTO.getOrganizerEmail()));
        eventModel.setExternalLink(eventDTO.getExternalLink());
        Set<String> tags = eventDTO.getTags();
        Set<Tag> finalTags = new HashSet<>();
        for (String tagName : tags) {
            if (tags != null) {
                finalTags.add(tagService.findOrCreateTag(tagName));
            }
        }
        eventModel.setTags(finalTags);
        eventModel.setRegistrationDeadline(eventDTO.getRegistrationDeadline());
        eventModel.setTitle(eventDTO.getTitle());
        eventModel.setMainImageUrl(eventDTO.getMainImageUrl());
        eventModel.setShortDescription(eventDTO.getShortDescription());
        return eventModel;
    }

    public EventRequestDTO mapToEventRequestDTO(EventRequest eventRequest) {
        EventRequestDTO eventRequestDTO = new EventRequestDTO();
        eventRequestDTO.setId(eventRequest.getId());
        eventRequestDTO.setTitle(eventRequest.getTitle());
        eventRequestDTO.setShortDescription(eventRequest.getShortDescription());
        eventRequestDTO.setFullDescription(eventRequest.getFullDescription());
        eventRequestDTO.setTags(eventRequest.getTags() != null
                ? eventRequest.getTags().stream().map(Tag::getName).collect(Collectors.toSet())
                : Collections.emptySet());
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
        eventRequest.setTags(eventRequestDTO.getTags() != null
                ? eventRequestDTO.getTags().stream().map(tagService::findOrCreateTag).collect(Collectors.toSet())
                : Collections.emptySet());
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
}
