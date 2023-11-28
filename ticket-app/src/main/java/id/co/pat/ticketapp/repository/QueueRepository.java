package id.co.pat.ticketapp.repository;

import id.co.pat.ticketapp.model.Queue;
import id.co.pat.ticketapp.model.enums.QueueStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QueueRepository extends CrudRepository<Queue, Long> {
    Optional<Queue> findFirstByTicketIdAndQueueStatus(Long ticketId, QueueStatus queueStatus);
}
