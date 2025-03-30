package org.ngcvfb.eventhubkz.Repository;

import org.ngcvfb.eventhubkz.DTO.NotificationMessage;
import org.ngcvfb.eventhubkz.Models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserEmail(String email);
}
