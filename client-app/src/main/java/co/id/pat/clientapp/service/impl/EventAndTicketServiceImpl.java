package co.id.pat.clientapp.service.impl;

import co.id.pat.clientapp.dto.EventResponse;
import co.id.pat.clientapp.dto.HoldTicketResponse;
import co.id.pat.clientapp.dto.TicketResponse;
import co.id.pat.clientapp.exception.BadRequestException;
import co.id.pat.clientapp.model.Account;
import co.id.pat.clientapp.model.TicketBookingHistory;
import co.id.pat.clientapp.model.enums.BookingStatus;
import co.id.pat.clientapp.repository.AccountRepository;
import co.id.pat.clientapp.repository.TicketBookingHistoryRepository;
import co.id.pat.clientapp.service.EventAndTicketService;
import co.id.pat.clientapp.service.impl.feign.FeignTicket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventAndTicketServiceImpl implements EventAndTicketService {

    private final FeignTicket feignTicket;
    private final TicketBookingHistoryRepository ticketBookingHistoryRepository;
    private final AccountRepository accountRepository;
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
    public HoldTicketResponse holdATicket(long eventId, long ticketId, long accountId) {
        HoldTicketResponse holdTicketResponse = feignTicket.holdATicket(eventId, ticketId);

        String json = "";
        try {
            json = objectMapper.writeValueAsString(holdTicketResponse);
        } catch (JsonProcessingException e) {

        }

        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new BadRequestException("Account does not exist");
        }

        TicketBookingHistory history = TicketBookingHistory
                .builder()
                .ticketId(ticketId)
                .accountId(accountId)
                .bookingStatus(BookingStatus.WAITING)
                .holdResponse(json)
                .build();
        ticketBookingHistoryRepository.save(history);

        holdTicketResponse.setBookingId(history.getId());
        return holdTicketResponse;
    }
}
