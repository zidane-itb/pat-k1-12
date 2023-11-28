package id.co.pat.ticketapp.service;

import id.co.pat.ticketapp.dto.HoldTicketResponse;
import id.co.pat.ticketapp.dto.TicketResponse;
import id.co.pat.ticketapp.model.enums.TicketStatus;

import java.util.List;

public interface TicketService {

    List<TicketResponse> getEventAvailableTickets(long eventId);
    HoldTicketResponse holdTicketAndGetInvoiceNumber(long ticketId);
    void updateTicketStatus(long ticketId, TicketStatus status);
    boolean checkTicketEligibility(long ticketId, long eventId);
    boolean checkTicketExists(long ticketId);

}
