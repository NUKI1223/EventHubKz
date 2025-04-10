package org.ngcvfb.eventhubkz.Services;


import org.ngcvfb.eventhubkz.Config.RabbitMQConfig;
import org.ngcvfb.eventhubkz.DTO.NotificationMessage;
import org.ngcvfb.eventhubkz.Models.Notification;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Repository.NotificationRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@EnableScheduling
public class NotificationProducer {
    private final AmqpTemplate amqpTemplate;
    private final EventService eventService;
    private final UserService userService;
    private final NotificationRepository notificationRepository;
    public NotificationProducer(AmqpTemplate amqpTemplate, EventService eventService, UserService userService, NotificationRepository notificationRepository) {
        this.amqpTemplate = amqpTemplate;
        this.eventService = eventService;
        this.userService = userService;
        this.notificationRepository = notificationRepository;
    }

    public void sendNotification(NotificationMessage notification) {
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, notification);
        System.out.println("Send notification message: " + notification);
    }
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void sendScheduledNotification() {
        List<UserModel> users = new ArrayList<>(userService.getAllUsers());

        for (UserModel user : users) {
            boolean error = false;
            String recommendedEventLinks = eventService.getSimilarEvent(user.getId());
            if (Objects.equals(recommendedEventLinks, "No recommended events.")) {
                error = true;
                System.out.println(recommendedEventLinks);
            }
            if (Objects.equals(recommendedEventLinks, "No tags found from user's liked events."))
            {
                error = true;
                System.out.println(recommendedEventLinks);
            }
            if (Objects.equals(recommendedEventLinks, "User has not liked any events.")) {
                error = true;
                System.out.println(recommendedEventLinks);
            }
            if (!error) {
                if (!recommendedEventLinks.isEmpty()) {
                    String message = "We've found an event that you might enjoy: ";
                    Notification notification = new Notification(
                            user.getUsername(),
                            message,
                            recommendedEventLinks,
                            LocalDateTime.now(),
                            false
                    );
                    notificationRepository.save(notification);
                    System.out.println("Send notification message: " + notification);
                }
            }
        }

    }


}
