package org.ngcvfb.eventhubkz.Models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    @Column(nullable = false)
    private String message;

    private String link;

    private LocalDateTime createdAt;

    private boolean read;

    public Notification() {
    }

    public Notification(String userEmail, String message, String link, LocalDateTime createdAt, boolean read) {
        this.userEmail = userEmail;
        this.message = message;
        this.link = link;
        this.createdAt = createdAt;
        this.read = read;
    }
}
