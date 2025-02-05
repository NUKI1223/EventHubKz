package org.ngcvfb.kagglekz.DTO;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class EventDTO {
    private Long id;                  // ID мероприятия
    private String title;             // Название мероприятия
    private String shortDescription;  // Краткое описание
    private String fullDescription;   // Полное описание
    private Set<String> tags;         // Теги мероприятия
    private String location;          // Локация
    private boolean online;           // Онлайн/оффлайн
    private LocalDateTime eventDate;  // Дата проведения
    private LocalDateTime registrationDeadline; // Дата окончания регистрации
    private String mainImageUrl;      // Основное изображение
    private String externalLink;      // Внешняя ссылка
    private String organizerEmail;         // ID организатора

    public EventDTO(Long id, String title, String shortDescription, String location, boolean online,
                    LocalDateTime eventDate, LocalDateTime registrationDeadline, String mainImageUrl,
                    String externalLink, Set<String> tags) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.location = location;
        this.online = online;
        this.eventDate = eventDate;
        this.registrationDeadline = registrationDeadline;
        this.mainImageUrl = mainImageUrl;
        this.externalLink = externalLink;
        this.tags = tags;
    }

    public EventDTO() {

    }

    public EventDTO(Long id, String title, String shortDescription, LocalDateTime eventDate) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.eventDate = eventDate;
    }
}
