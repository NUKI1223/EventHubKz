package org.ngcvfb.kagglekz.Services;


import jakarta.persistence.criteria.Join;
import org.ngcvfb.kagglekz.Models.EventModel;
import org.ngcvfb.kagglekz.Models.Tag;
import org.ngcvfb.kagglekz.Repository.EventRepository;
import org.ngcvfb.kagglekz.Utils.MappingUtils;
import org.springframework.stereotype.Service;
import org.ngcvfb.kagglekz.DTO.EventDTO;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final TagService tagService;
    private final UserService userService;
    private final MappingUtils mappingUtils;


    public EventService(EventRepository eventRepository, TagService tagService, UserService userService, MappingUtils mappingUtils) {
        this.eventRepository = eventRepository;
        this.tagService = tagService;
        this.userService = userService;
        this.mappingUtils = mappingUtils;
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
    public List<EventModel> getEvents() {
        return eventRepository.findAll();
    }
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
        // Преобразование EventDto в EventModel
        EventModel event = mappingUtils.mapToEventModel(eventDto);

        // Привязка тегов
        Set<Tag> tags = eventDto.getTags().stream()
                .map(tagService::findOrCreateTag)
                .collect(Collectors.toSet());
        event.setTags(tags);

        // Сохранение мероприятия
        EventModel savedEvent = eventRepository.save(event);

        // Преобразование EventModel в EventDto
        EventDTO resultDto = mappingUtils.mapToEventDTO(savedEvent);

        return resultDto;
    }



}
