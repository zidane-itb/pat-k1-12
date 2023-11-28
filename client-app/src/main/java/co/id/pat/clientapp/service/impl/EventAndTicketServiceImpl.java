package co.id.pat.clientapp.service.impl;

import co.id.pat.clientapp.dto.EventResponse;
import co.id.pat.clientapp.dto.HoldTicketResponse;
import co.id.pat.clientapp.dto.TicketResponse;
import co.id.pat.clientapp.model.TicketBookingHistory;
import co.id.pat.clientapp.model.enums.BookingStatus;
import co.id.pat.clientapp.repository.TicketBookingHistoryRepository;
import co.id.pat.clientapp.service.EventAndTicketService;
import co.id.pat.clientapp.service.impl.feign.FeignTicket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventAndTicketServiceImpl implements EventAndTicketService {

    private final FeignTicket feignTicket;
    private final TicketBookingHistoryRepository ticketBookingHistoryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<EventResponse> getEvents() {
        return feignTicket.getEvents();
    }

    @Override
    public List<TicketResponse> getAvailableTickets(long eventId) {
        return feignTicket.getAvailableTickets(eventId);
    }

    @Override
    @Transactional
    public HoldTicketResponse holdATicket(long eventId, long ticketId) {
        HoldTicketResponse holdTicketResponse = feignTicket.holdATicket(eventId, ticketId);

        String json = "";
        try {
            json = objectMapper.writeValueAsString(holdTicketResponse);
        } catch (JsonProcessingException e) {

        }

        TicketBookingHistory history = TicketBookingHistory
                .builder()
                .ticketId(ticketId)
                .accountId(1L)
                .bookingStatus(BookingStatus.WAITING)
                .holdResponse(json)
                .build();
        ticketBookingHistoryRepository.save(history);

        holdTicketResponse.setBookingId(history.getId());
        return holdTicketResponse;
    }
}
