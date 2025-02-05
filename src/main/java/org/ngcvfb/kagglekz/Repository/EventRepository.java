package org.ngcvfb.kagglekz.Repository;

import org.ngcvfb.kagglekz.Models.EventModel;
import org.ngcvfb.kagglekz.Models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
