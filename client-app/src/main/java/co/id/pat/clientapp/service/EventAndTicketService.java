package co.id.pat.clientapp.service;

import co.id.pat.clientapp.dto.EventResponse;
import co.id.pat.clientapp.dto.HoldTicketResponse;
import co.id.pat.clientapp.dto.TicketResponse;

import java.util.List;

public interface EventAndTicketService {

    List<EventResponse> getEvents();
    List<TicketResponse> getAvailableTickets(long eventId);
    HoldTicketResponse holdATicket(long eventId, long ticketId);

}
