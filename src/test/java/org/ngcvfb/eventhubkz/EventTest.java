package org.ngcvfb.eventhubkz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ngcvfb.eventhubkz.DTO.EventDTO;
import org.ngcvfb.eventhubkz.Models.EventModel;
import org.ngcvfb.eventhubkz.Models.Role;
import org.ngcvfb.eventhubkz.Models.Tag;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Repository.EventRepository;
import org.ngcvfb.eventhubkz.Repository.TagRepository;
import org.ngcvfb.eventhubkz.Repository.UserRepository;
import org.ngcvfb.eventhubkz.Services.EventLikeService;
import org.ngcvfb.eventhubkz.Services.EventService;
import org.ngcvfb.eventhubkz.Services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class EventTest {
    @Autowired
    EventService eventService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventLikeService eventLikeService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TagService tagService;

    EventModel testEvent;

    UserModel testUser;

    @BeforeEach
    void setUp() {
        testUser = userRepository.findByEmail("test@example.com").orElseGet(() -> {
            UserModel user = UserModel.builder()
                    .username("testuser")
                    .email("test@example.com")
                    .password("secret")
                    .role(Role.USER)
                    .enabled(true)
                    .build();
            return userRepository.save(user);
        });
        tagService.createTags(List.of("Innovation", "Hackathon", "AI"));
        testEvent = EventModel.builder()
                .title("Test Event")
                .fullDescription("This is a test event.(Full Description)")
                .shortDescription("This is a test event(ShortDescription)")
                .location("Test City")
                .organizer(testUser)
                .eventDate(LocalDateTime.of(2025, 5, 10, 18, 0))  // Дата начала
                .registrationDeadline(LocalDateTime.of(2025, 5, 11, 23, 0))    // Дата окончания
                .tags(new HashSet<>(tagService.findOrCreateTags(List.of("AI", "Hackathon", "Java"))))
                .mainImageUrl("someUrl")
                .build();
        eventRepository.save(testEvent);
        eventLikeService.addLike(testEvent.getId(), testUser.getId());

    }
    @Test
    public void testFindAll() {
        List<EventDTO> eventList = eventService.getAllEvents();
        assertFalse(eventList.isEmpty(), "Event list is empty");
        assertTrue(eventList.stream().anyMatch(u -> u.getTitle().equals("Test Event")));
    }

    @Test
    public void testFindById() {
        EventDTO eventDTO = eventService.getEvent(testEvent.getId());
        assertNotNull(eventDTO, "testEventId should not be null");
        assertEquals(testEvent.getTitle(), eventDTO.getTitle());
    }
    @Test
    public void testFindByTags() {
        List<EventDTO> eventList = eventService.getEventsByTags(testEvent.getTags());
        assertFalse(eventList.isEmpty(), "Event list is empty");
        assertTrue(eventList.stream().anyMatch(u -> u.getTitle().equals("Test Event")));
    }
    @Test
    public void testUpdate() {
        EventDTO eventDTO = eventService.getEvent(testEvent.getId());
        eventDTO.setTitle("Updated Title");
        assertNotNull(eventDTO, "testEventId should not be null");
        assertEquals("Updated Title", eventDTO.getTitle());
    }
    @Test
    public void testRecomendation(){
        EventModel eventModel = EventModel.builder()
                .title("Test Event2")
                .fullDescription("This is a test event.2(Full Description)")
                .shortDescription("This is a test event2(ShortDescription)")
                .organizer(testUser)
                .location("Test City")
                .eventDate(LocalDateTime.of(2026, 5, 10, 18, 0))  // Дата начала
                .registrationDeadline(LocalDateTime.of(2026, 5, 11, 23, 0))    // Дата окончания
                .tags(new HashSet<>(tagService.findOrCreateTags(List.of("AI", "Hackathon", "Java"))))
                .mainImageUrl("someUrl")
                .build();
        eventRepository.save(eventModel);
        String url = eventService.getSimilarEvent(testUser.getId());
        assertNotNull(url, "Wrong recomendation");
    }
    @Test
    public void testDelete() {
        EventDTO eventDTO = eventService.getEvent(testEvent.getId());
        eventService.deleteEvent(eventDTO.getId());
        List<EventDTO> eventList = eventService.getAllEvents();
        assertFalse(eventList.isEmpty(), "Event list is empty");
    }

}
