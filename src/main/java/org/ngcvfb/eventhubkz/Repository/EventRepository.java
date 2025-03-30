package org.ngcvfb.eventhubkz.Repository;

import org.ngcvfb.eventhubkz.Models.EventModel;
import org.ngcvfb.eventhubkz.Models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Long> {
    @Query("SELECT e FROM EventModel e JOIN e.tags t WHERE t IN :tags GROUP BY e HAVING COUNT(t) = :tagCount")
    List<EventModel> findAllByTags(@Param("tags") Set<Tag> tags, @Param("tagCount") long tagCount);


}
