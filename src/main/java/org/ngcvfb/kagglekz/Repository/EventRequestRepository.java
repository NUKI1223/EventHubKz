package org.ngcvfb.kagglekz.Repository;

import org.ngcvfb.kagglekz.Models.EventRequest;
import org.ngcvfb.kagglekz.Models.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    List<EventRequest> findByStatus(RequestStatus status); // Для фильтрации по статусу
    List<EventRequest> findByRequesterId(Long requesterId);
}
