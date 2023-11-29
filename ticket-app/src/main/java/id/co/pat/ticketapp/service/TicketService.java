package id.co.pat.ticketapp.service;

import id.co.pat.ticketapp.dto.HoldTicketResponse;
import id.co.pat.ticketapp.dto.TicketResponse;
import id.co.pat.ticketapp.model.Invoice;
import id.co.pat.ticketapp.model.Ticket;
import id.co.pat.ticketapp.model.enums.TicketStatus;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    List<TicketResponse> getEventAvailableTickets(long eventId);
    HoldTicketResponse holdTicketAndGetInvoiceNumber(long ticketId);
    void updateTicketStatus(long ticketId, TicketStatus status);
    boolean checkTicketEligibility(long ticketId, long eventId);
    boolean checkTicketExists(long ticketId);
    HoldTicketResponse handleFailedInvoice(long ticketId, long invoiceNumber);
    Optional<Ticket> findTicketById(long ticketId);

}
