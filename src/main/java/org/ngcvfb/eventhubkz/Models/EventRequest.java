package org.ngcvfb.eventhubkz.Models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "event_requests")
public class EventRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String shortDescription;

    @Lob
    @Column(nullable = false)
    private String fullDescription;

    @ManyToMany
    @JoinTable(
            name = "event_request_tags",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private boolean online;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    private LocalDateTime registrationDeadline;

    private String mainImageUrl;

    private String externalLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(length = 1000)
    private String adminComment;
}
