package org.ngcvfb.eventhubkz.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class EventModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String shortDescription;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    @JsonIgnore
    private String fullDescription;

    @ManyToMany
    @JoinTable(
            name = "event_tags",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonManagedReference
    private Set<Tag> tags = new HashSet<>();

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private boolean online;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    private LocalDateTime registrationDeadline;

    @Column(nullable = false)
    private String mainImageUrl;

    private String externalLink;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id", nullable = false)
    private UserModel organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventLike> likes;


    @Override
    public String toString() {
        return "EventModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", fullDescription='" + fullDescription + '\'' +
                ", tags=" + tags +
                ", location='" + location + '\'' +
                ", online=" + online +
                ", eventDate=" + eventDate +
                ", registrationDeadline=" + registrationDeadline +
                ", mainImageUrl='" + mainImageUrl + '\'' +
                ", externalLink='" + externalLink + '\'' +
                ", organizer=" + organizer +
                ", likes=" + likes +
                '}';
    }
}
