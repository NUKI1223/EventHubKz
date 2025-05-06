package org.ngcvfb.eventhubkz.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ngcvfb.eventhubkz.Models.Notification;
import org.ngcvfb.eventhubkz.Repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Notification", description = "RabbitMq notification API")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Operation(summary = "Getting all user notification by username")
    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam String username) {
        List<Notification> notifications = notificationRepository.findAllByUserEmail(username);
        notificationRepository.saveAll(notifications);
        return ResponseEntity.ok(notifications);
    }
    @Operation(summary = "Updating notification")
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) {
            return ResponseEntity.notFound().build();
        }
        notification.setRead(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok(notification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) {
            return ResponseEntity.notFound().build();
        }
        notificationRepository.delete(notification);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/all")
    @Transactional
    public ResponseEntity<Void> deleteAllNotifications(@RequestParam String username) {
        notificationRepository.deleteAllByUserEmail(username);
        return ResponseEntity.ok().build();
    }
}
