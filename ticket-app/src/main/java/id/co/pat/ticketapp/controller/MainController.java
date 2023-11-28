package id.co.pat.ticketapp.controller;

import id.co.pat.ticketapp.dto.EventResponse;
import id.co.pat.ticketapp.dto.HoldTicketResponse;
import id.co.pat.ticketapp.dto.TicketResponse;
import id.co.pat.ticketapp.service.EventService;
import id.co.pat.ticketapp.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final EventService eventService;
    private final TicketService ticketService;

    @GetMapping("/v1/event")
    public ResponseEntity<List<EventResponse>> getOnGoingEvents() {
        return ResponseEntity.ok()
                .body(eventService.getOnGoingEvents());
    }

    @GetMapping("/v1/event/{eventId}")
    public ResponseEntity<List<TicketResponse>> getAvailableTickets(@PathVariable("eventId") long eventId) {
        if (!eventService.checkEventExists(eventId)) {
            return ResponseEntity.badRequest().body(null);
        }
        List<TicketResponse> tickets = ticketService.getEventAvailableTickets(eventId);
        return ResponseEntity.ok()
                .body(tickets);
    }

    @PostMapping("/v1/event/{eventId}/ticket/{ticketId}")
    public ResponseEntity<HoldTicketResponse> holdATicket(@PathVariable("eventId") long eventId,
                                               @PathVariable("ticketId") long ticketId) {
        if (!ticketService.checkTicketEligibility(ticketId, eventId)) {
            log.info("ticket not eligible, with event id: {} and ticket id: {}", eventId, ticketId);
            return ResponseEntity.badRequest().body(null);
        }

        HoldTicketResponse holdTicketResponse = ticketService.holdTicketAndGetInvoiceNumber(ticketId);
        int status = holdTicketResponse == null ? 400: 200;
        return ResponseEntity.status(status)
                .body(holdTicketResponse);
    }

}
