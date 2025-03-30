package org.ngcvfb.eventhubkz.Services;


import org.springframework.transaction.annotation.Transactional;
import org.ngcvfb.eventhubkz.Models.EventLike;
import org.ngcvfb.eventhubkz.Models.EventModel;
import org.ngcvfb.eventhubkz.Models.Tag;
import org.ngcvfb.eventhubkz.Repository.EventCacheRepository;
import org.ngcvfb.eventhubkz.Repository.EventLikeRepository;
import org.ngcvfb.eventhubkz.Repository.EventRepository;
import org.ngcvfb.eventhubkz.Utils.MappingUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.ngcvfb.eventhubkz.DTO.EventDTO;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventCacheRepository eventCacheRepository;
    private final EventLikeRepository eventLikeRepository;
    private final EventSearchService eventSearch;
    private final TagService tagService;
    private final UserService userService;
    private final MappingUtils mappingUtils;


    public EventService(EventRepository eventRepository, TagService tagService, UserService userService, MappingUtils mappingUtils, EventSearchService eventSearch, EventCacheRepository eventCacheRepository, EventLikeRepository eventLikeRepository) {
        this.eventRepository = eventRepository;
        this.tagService = tagService;
        this.userService = userService;
        this.mappingUtils = mappingUtils;
        this.eventSearch = eventSearch;
        this.eventCacheRepository = eventCacheRepository;
        this.eventLikeRepository = eventLikeRepository;
    }

    public List<EventDTO> getAllEvents() {
        try {
            List<EventModel> events = eventRepository.findAll();
            List<EventDTO> dtos = new ArrayList<>();
            for (EventModel event : events) {
                dtos.add(mappingUtils.mapToEventDTO(event));
            }
            return dtos;
        }
        catch (ConcurrentModificationException e){
            System.out.println("ConcurrentModificationException");
        }
        return new ArrayList<>();
    }


    @Cacheable(value = "events", key = "#id")
    @Transactional
    public EventDTO getEvent(Long id) {
        EventModel event = eventRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event with id " + id + " not found"));

        return mappingUtils.mapToEventDTO(event);
    }
    public List<EventDTO> getEventsByTags(Set<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("Tags must not be null or empty");
        }

        List<EventModel> events = eventRepository.findAllByTags(tags, tags.size());

        return events.stream()
                .map(mappingUtils::mapToEventDTO
                ).collect(Collectors.toList());
    }

    public EventDTO createEvent(EventDTO eventDto) {

        EventModel event = mappingUtils.mapToEventModel(eventDto);

        Set<Tag> tags = tagService.findTags(eventDto.getTags().stream().toList());
        event.setTags(tags);

        EventModel savedEvent = eventRepository.save(event);
        eventSearch.saveEventToSearch(savedEvent);
        EventDTO resultDto = mappingUtils.mapToEventDTO(savedEvent);

        return resultDto;
    }

    public EventDTO updateEvent(Long id, EventDTO eventDto) {
        Optional<EventModel> eventModel = eventRepository.findById(id);
        if (eventModel.isPresent()) {
            EventModel updatedEvent = mappingUtils.updateEventModel(eventDto, eventModel.get());
            EventModel savedEvent = eventRepository.save(updatedEvent);
            eventSearch.saveEventToSearch(savedEvent);
            return mappingUtils.mapToEventDTO(savedEvent);
        }
        return eventDto;

    }

    @CacheEvict(value = "events", key = "#id")
    @Transactional
    public void deleteEvent(Long id) {
        EventModel event = eventRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event with id " + id + " not found"));
        eventRepository.delete(event);
        eventSearch.deleteEventFromSearch(id.toString());

    }


    @Transactional
    public String getSimilarEvent(Long userId){
        List<EventLike> likes = eventLikeRepository.findByUserId(userId);
        if (likes == null || likes.isEmpty()) {
            return "User has not liked any events.";
        }

        // Собираем ID мероприятий, которые пользователь уже лайкал
        Set<Long> likedEventIds = likes.stream()
                .map(like -> like.getEvent().getId())
                .collect(Collectors.toSet());


        Map<String, Integer> tagFrequency = new HashMap<>();
        for (EventLike like : likes) {
            EventModel event = like.getEvent();
            if (event != null && event.getTags() != null) {
                for (Tag tag : event.getTags()) {
                    String tagName = tag.getName();
                    tagFrequency.put(tagName, tagFrequency.getOrDefault(tagName, 0) + 1);
                }
            }
        }
        if (tagFrequency.isEmpty()) {
            throw new NoSuchElementException("No tags found from "+userService.getUserById(userId).getEmail() +"'s liked events.");
        }

        List<String> sortedTags = tagFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        Set<Tag> recommendedTags = new HashSet<>();
        if (sortedTags.size() >= 2) {
            recommendedTags.add(tagService.findTag(sortedTags.get(0)));
            recommendedTags.add(tagService.findTag(sortedTags.get(1)));
        } else {
            recommendedTags.add(tagService.findTag(sortedTags.get(0)));
        }

        List<EventModel> candidateEvents = eventRepository.findAllByTags(recommendedTags, recommendedTags.size());
        if (candidateEvents.isEmpty()) {
            return "No recommended events.";
        }

        List<EventModel> recommendedEvents = candidateEvents.stream()
                .filter(event -> !likedEventIds.contains(event.getId()))
                .collect(Collectors.toList());

        if (recommendedEvents.isEmpty()) {
            System.out.println("No events");
            return "No recommended events.";
        }


        int randIndex = ThreadLocalRandom.current().nextInt(recommendedEvents.size());
        EventModel recommendedEvent = recommendedEvents.get(randIndex);
        return "http://localhost:8080/api/events/" + recommendedEvent.getId();
    }
    }





