package org.ngcvfb.eventhubkz.Services;


import org.ngcvfb.eventhubkz.Models.EventLike;
import org.ngcvfb.eventhubkz.Repository.EventLikeRepository;
import org.ngcvfb.eventhubkz.Utils.MappingUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventLikeService {
    private final EventLikeRepository eventLikeRepository;
    private final UserService userService;
    private final EventService eventService;
    private final MappingUtils mappingUtils;
    public EventLikeService(EventLikeRepository eventLikeRepository, UserService userService, EventService eventService, MappingUtils mappingUtils) {
        this.eventLikeRepository = eventLikeRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.mappingUtils = mappingUtils;
    }

    public String addLike(Long eventId, Long userId) {
        Optional<EventLike> eventLike = eventLikeRepository.findByUserIdAndEventId(userId, eventId);
        if (eventLike.isPresent()) {
            deleteLike(eventId, userId);
            return "deleted";
        }
        else {
            EventLike newLike = new EventLike();
            newLike.setEvent(mappingUtils.mapToEventModel(eventService.getEvent(eventId)));
            newLike.setUser(userService.getUserById(userId));
            eventLikeRepository.save(newLike);
            return "liked";
        }

    }
    public String deleteLike(Long eventId, Long userId) {
        EventLike eventLike = eventLikeRepository.findByUserIdAndEventId(userId, eventId).orElse(null);
        if (eventLike != null) {
            eventLikeRepository.delete(eventLike);
            return "deleted";
        }
        else{
            addLike(eventId, userId);
            return "liked";
        }

    }

}
