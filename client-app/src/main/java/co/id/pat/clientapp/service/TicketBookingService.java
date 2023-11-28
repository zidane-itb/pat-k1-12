package co.id.pat.clientapp.service;

import co.id.pat.clientapp.model.TicketBookingHistory;

import java.util.List;
import java.util.Optional;

public interface TicketBookingService {

    Optional<TicketBookingHistory> getTicketBookingHistoryByTicketId(Long ticketId);
    void invalidateAllWaiting(Long ticketId, Long successId);
    void updateTicketBooking(TicketBookingHistory ticketBookingHistory);
    Optional<TicketBookingHistory> findNextInLine(Long ticketId, Long successId);
    Optional<TicketBookingHistory> findById(Long bookingId);

}
