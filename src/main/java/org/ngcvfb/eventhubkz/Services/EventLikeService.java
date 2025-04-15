package org.ngcvfb.eventhubkz.Services;


import org.ngcvfb.eventhubkz.DTO.EventDTO;
import org.ngcvfb.eventhubkz.DTO.UserDTO;
import org.ngcvfb.eventhubkz.Models.EventLike;
import org.ngcvfb.eventhubkz.Models.EventModel;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Repository.EventLikeRepository;
import org.ngcvfb.eventhubkz.Repository.EventRepository;
import org.ngcvfb.eventhubkz.Utils.MappingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EventLikeService {
    private final EventLikeRepository eventLikeRepository;
    private final NotificationProducer notificationProducer;

    private final UserService userService;
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final MappingUtils mappingUtils;
    public EventLikeService(EventLikeRepository eventLikeRepository, UserService userService, EventService eventService, MappingUtils mappingUtils, NotificationProducer notificationProducer, EventRepository eventRepository) {
        this.eventLikeRepository = eventLikeRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.mappingUtils = mappingUtils;
        this.notificationProducer = notificationProducer;
        this.eventRepository = eventRepository;
    }



    public String addLike(Long eventId, Long userId) {
        Optional<EventLike> existingLike = eventLikeRepository.findByUserIdAndEventId(userId, eventId);
        if (existingLike.isPresent()) {
            return "event is already liked";
        } else {

            UserModel user = userService.getUserById(userId);
            EventModel event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NoSuchElementException("Event not found"));

            EventLike newLike = new EventLike();
            newLike.setEvent(event);
            newLike.setUser(user);

            eventLikeRepository.save(newLike);
            eventLikeRepository.flush();
            return "liked";
        }
    }



    @Transactional
    public String deleteLike(Long eventId, Long userId) {
        EventLike eventLike = eventLikeRepository.findByUserIdAndEventId(userId, eventId).orElse(null);
        if (eventLike != null) {
            eventLikeRepository.deleteEventLikeById(eventLike.getId());
            eventLikeRepository.flush();
            return "deleted";
        }
        else{
            return "event is already unliked";
        }

    }

    public int getEventLikeCount(Long eventId) {
        return eventLikeRepository.findByEventId(eventId).size();
    }
    public List<EventDTO> getEventLikes(Long userId) {
        List<EventLike> eventLikes = eventLikeRepository.findByUserId(userId);
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (EventLike eventLike : eventLikes) {
            EventDTO eventDTO = mappingUtils.mapToEventDTO(eventLike.getEvent());
            eventDTOs.add(eventDTO);
        }
        return eventDTOs;
    }

    public List<UserDTO> getUsersFromLikedEvent(Long eventId) {
        List<EventLike> eventLikes = eventLikeRepository.findByEventId(eventId);
        List<UserDTO> users = new ArrayList<>();
        for (EventLike eventLike : eventLikes) {
            UserModel user = userService.getUserById(eventLike.getUser().getId());
            users.add(mappingUtils.mapToUserDTO(user));
        }
        return users;

    }

}
