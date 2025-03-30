package org.ngcvfb.eventhubkz.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@RedisHash(value = "events", timeToLive = 3600)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCache implements Serializable {
    private Long id;
    private String title;
    private String shortDescription;
    private Set<String> tags;
    private String location;
    private boolean online;
    private int likeCount;
    private LocalDateTime creationDate;
}
