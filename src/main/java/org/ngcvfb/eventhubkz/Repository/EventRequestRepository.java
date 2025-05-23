package org.ngcvfb.eventhubkz.Repository;

import org.ngcvfb.eventhubkz.Models.EventRequest;
import org.ngcvfb.eventhubkz.Models.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    List<EventRequest> findByStatus(RequestStatus status);
    List<EventRequest> findByRequesterId(Long requesterId);
}
