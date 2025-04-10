package org.ngcvfb.eventhubkz;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ngcvfb.eventhubkz.Models.EventModel;
import org.ngcvfb.eventhubkz.Models.Notification;
import org.ngcvfb.eventhubkz.Models.Role;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Repository.EventRepository;
import org.ngcvfb.eventhubkz.Repository.NotificationRepository;
import org.ngcvfb.eventhubkz.Repository.UserRepository;
import org.ngcvfb.eventhubkz.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@Transactional
public class NotificationTest {
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    NotificationProducer notificationProducer;

    @Autowired
    NotificationConsumer notificationConsumer;

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

    EventModel testEventLike;

    UserModel testUser;

    @BeforeEach
    public void setUp() {
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
                .tags(new HashSet<>(tagService.findOrCreateTags(List.of("Innovation", "Hackathon", "AI"))))
                .mainImageUrl("someUrl")
                .build();
        testEventLike = EventModel.builder()
                .title("Test Event2")
                .fullDescription("This is a test event2.(Full Description)")
                .shortDescription("This is a test event2(ShortDescription)")
                .location("Test City2")
                .organizer(testUser)
                .eventDate(LocalDateTime.of(2026, 5, 10, 18, 0))  // Дата начала
                .registrationDeadline(LocalDateTime.of(2026, 5, 11, 23, 0))    // Дата окончания
                .tags(new HashSet<>(tagService.findOrCreateTags(List.of("Innovation", "Hackathon", "AI"))))
                .mainImageUrl("someUrl")
                .build();
        eventRepository.save(testEvent);
        eventRepository.save(testEventLike);
        eventLikeService.addLike(testEvent.getId(), testUser.getId());

    }

    @Test
    void sendNotification(){
        notificationProducer.sendScheduledNotification();
        List<String> notif = notificationRepository.findAllByUserEmail(testUser.getUsername()).stream().map(Notification::getLink).toList();
        assertNotNull(notif.getFirst());
    }

}
