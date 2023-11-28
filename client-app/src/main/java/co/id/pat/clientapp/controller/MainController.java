package co.id.pat.clientapp.controller;

import co.id.pat.clientapp.dto.EventResponse;
import co.id.pat.clientapp.dto.HoldTicketResponse;
import co.id.pat.clientapp.dto.TicketResponse;
import co.id.pat.clientapp.service.EventAndTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final EventAndTicketService eventAndTicketService;

    @GetMapping("/v1/client/event")
    public ResponseEntity<List<EventResponse>> getOnGoingEvents() {
        return ResponseEntity.ok(eventAndTicketService.getEvents());
    }

    @GetMapping("/v1/client/event/{eventId}")
    public ResponseEntity<List<TicketResponse>> getAvailableTickets(@PathVariable("eventId") long eventId) {
        return ResponseEntity.ok(eventAndTicketService.getAvailableTickets(eventId));
    }

    @PostMapping("/v1/client/event/{eventId}/ticket/{ticketId}")
    public ResponseEntity<HoldTicketResponse> holdATicket(@PathVariable("eventId") long eventId,
                                                          @PathVariable("ticketId") long ticketId) {
        return ResponseEntity.ok(eventAndTicketService.holdATicket(eventId, ticketId));
    }

}
