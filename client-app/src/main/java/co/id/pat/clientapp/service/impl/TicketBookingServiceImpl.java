package co.id.pat.clientapp.service.impl;

import co.id.pat.clientapp.model.TicketBookingHistory;
import co.id.pat.clientapp.model.enums.BookingStatus;
import co.id.pat.clientapp.repository.TicketBookingHistoryRepository;
import co.id.pat.clientapp.service.TicketBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TicketBookingServiceImpl implements TicketBookingService {

    private final TicketBookingHistoryRepository ticketBookingHistoryRepository;

    @Override
    public Optional<TicketBookingHistory> getTicketBookingHistoryByTicketId(Long ticketId) {
        return ticketBookingHistoryRepository.findFirstByTicketIdAndStatusWaiting(ticketId);
    }

    @Override
    public void invalidateAllWaiting(Long ticketId, Long successId) {
        List<TicketBookingHistory> ticketBookingHistoryList = ticketBookingHistoryRepository
                .findAllByTicketIdAndNotEqualToIdAndWaiting(ticketId, successId);
        for (TicketBookingHistory t: ticketBookingHistoryList) {
            t.setBookingStatus(BookingStatus.FAILED);
        }
        ticketBookingHistoryRepository.saveAll(ticketBookingHistoryList);
    }

    @Override
    public void updateTicketBooking(TicketBookingHistory ticketBookingHistory) {
        ticketBookingHistoryRepository.save(ticketBookingHistory);
    }

    @Override
    public Optional<TicketBookingHistory> findNextInLine(Long ticketId, Long successId) {
        return ticketBookingHistoryRepository.findFirstByTicketIdAndNotEqualToIdAndWaiting(ticketId, successId);
    }

    @Override
    public Optional<TicketBookingHistory> findById(Long bookingId) {
        return ticketBookingHistoryRepository.findById(bookingId);
    }
}
