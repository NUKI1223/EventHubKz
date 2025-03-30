package org.ngcvfb.eventhubkz.Repository;

import org.ngcvfb.eventhubkz.Models.EventCache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCacheRepository extends JpaRepository<EventCache, Long> {
    public void deleteByTitle(String title);
}
