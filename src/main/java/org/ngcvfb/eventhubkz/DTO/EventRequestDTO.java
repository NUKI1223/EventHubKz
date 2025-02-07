package org.ngcvfb.eventhubkz.DTO;

import lombok.Getter;
import lombok.Setter;
import org.ngcvfb.eventhubkz.Models.RequestStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class EventRequestDTO {
    private Long id;
    private String title;
    private String shortDescription;
    private String fullDescription;
    private Set<String> tags; // Tag names
    private String location;
    private boolean online;
    private LocalDateTime eventDate;
    private LocalDateTime registrationDeadline;
    private String mainImageUrl;
    private String externalLink;
    private String requesterEmail; // Email of the user who created the request
    private String adminComment;  // Optional comment from admin
    private RequestStatus status; // PENDING, APPROVED, REJECTED


    public EventRequestDTO() {
    }

    public EventRequestDTO(Long id, String title, String shortDescription, String fullDescription, Set<String> tags, String location, boolean online, LocalDateTime eventDate, LocalDateTime registrationDeadline, String mainImageUrl, String externalLink, String requesterEmail, String adminComment, RequestStatus status) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.tags = tags;
        this.location = location;
        this.online = online;
        this.eventDate = eventDate;
        this.registrationDeadline = registrationDeadline;
        this.mainImageUrl = mainImageUrl;
        this.externalLink = externalLink;
        this.requesterEmail = requesterEmail;
        this.adminComment = adminComment;
        this.status = status;
    }
}
