package id.co.pat.ticketapp.repository;

import id.co.pat.ticketapp.model.Ticket;
import id.co.pat.ticketapp.model.enums.TicketStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

    @Query(value = "SELECT * FROM ticket WHERE (ticket_status = 'OPEN' or ticket_status = 'ON_GOING') AND event_id = :eventId",
            nativeQuery = true)
    List<Ticket> findAvailableTickets(long eventId);

    Optional<Ticket> findByIdAndTicketStatus(long id, TicketStatus status);
    @Query(value = "select EXISTS(SELECT * FROM ticket WHERE event_id = :eventId AND ticket_id = :id " +
            "and (ticket_status = 'OPEN' or ticket_status = 'ON_GOING'))",
            nativeQuery = true)
    boolean existsByIdAndEventIdAndOpen(long id, long eventId);

}
