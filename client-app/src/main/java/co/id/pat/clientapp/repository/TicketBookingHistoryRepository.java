package co.id.pat.clientapp.repository;

import co.id.pat.clientapp.model.TicketBookingHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TicketBookingHistoryRepository extends CrudRepository<TicketBookingHistory, Long> {
    @Query(value = "SELECT * FROM ticket_booking_history WHERE ticket_id = :ticketId " +
            "AND booking_status = 'WAITING' LIMIT 1 ORDER BY ticket_booking_history_id ASC",
            nativeQuery = true)
    Optional<TicketBookingHistory> findFirstByTicketIdAndStatusWaiting(Long ticketId);
    @Query(value = "SELECT * FROM ticket_booking_history WHERE ticket_id = :ticketId " +
            "AND ticket_booking_history_id <> :id AND booking_status = 'WAITING'",
            nativeQuery = true)
    List<TicketBookingHistory> findAllByTicketIdAndNotEqualToIdAndWaiting(Long ticketId, Long id);

    @Query(value = "SELECT * FROM ticket_booking_history WHERE ticket_id = :ticketId " +
            "AND ticket_booking_history_id <> :id " +
            "and booking_status = 'WAITING' ORDER BY ticket_booking_history_id ASC LIMIT 1",
            nativeQuery = true)
    Optional<TicketBookingHistory> findFirstByTicketIdAndNotEqualToIdAndWaiting(Long ticketId, Long id);
}
