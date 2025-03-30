package org.ngcvfb.eventhubkz.Repository;

import org.ngcvfb.eventhubkz.Models.EventLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventLikeRepository extends JpaRepository<EventLike, Long> {
    public List<EventLike> findByEventId(Long eventId);
    public List<EventLike> findByUserId(Long userId);
    public Optional<EventLike> findByUserIdAndEventId(Long userId, Long eventId);

    @Modifying
    @Query("DELETE FROM EventLike e WHERE e.id = :id")
    void deleteEventLikeById(@Param("id") Long id);

}
