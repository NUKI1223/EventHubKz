package org.ngcvfb.eventhubkz.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "event_tags",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @JsonBackReference
    private Set<EventModel> events = new HashSet<>();


    public Set<EventModel> getEvents() {
        return events == null ? Collections.emptySet() : new HashSet<>(events);
    }

    public void setEvents(Set<EventModel> events) {
        this.events = events == null ? new HashSet<>() : new HashSet<>(events);
    }
}