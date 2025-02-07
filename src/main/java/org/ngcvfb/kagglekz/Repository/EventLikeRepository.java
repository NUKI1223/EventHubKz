package org.ngcvfb.kagglekz.Repository;

import org.ngcvfb.kagglekz.Models.EventLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventLikeRepository extends JpaRepository<EventLike, Long> {
    public List<EventLike> findByEventId(Long eventId);
    public List<EventLike> findByUserId(Long userId);
}
