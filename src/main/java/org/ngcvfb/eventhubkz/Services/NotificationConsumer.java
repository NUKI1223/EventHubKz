package org.ngcvfb.eventhubkz.Services;


import org.ngcvfb.eventhubkz.Config.RabbitMQConfig;
import org.ngcvfb.eventhubkz.DTO.NotificationMessage;
import org.ngcvfb.eventhubkz.Models.Notification;
import org.ngcvfb.eventhubkz.Repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;

    public NotificationConsumer(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveNotification(NotificationMessage message) {
        System.out.println("Received message: "+message);
        String notificationText = "Мы обнаружили похожие мероприятия, которые могут вам понравиться!";
        String link = "http://localhost:8080/api/events/" + message.getEventId();
        Notification notification = new Notification(
                message.getEmailName(),
                notificationText,
                link,
                LocalDateTime.now(),
                false
        );
        notificationRepository.save(notification);
        System.out.println("Notification saved for user: "+message.getEmailName());
    }
}
