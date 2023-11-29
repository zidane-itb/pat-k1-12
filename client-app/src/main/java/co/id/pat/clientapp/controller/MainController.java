package co.id.pat.clientapp.controller;

import co.id.pat.clientapp.dto.EventResponse;
import co.id.pat.clientapp.dto.HoldTicketResponse;
import co.id.pat.clientapp.dto.TicketResponse;
import co.id.pat.clientapp.model.TicketBookingHistory;
import co.id.pat.clientapp.model.enums.BookingStatus;
import co.id.pat.clientapp.service.EventAndTicketService;
import co.id.pat.clientapp.service.TicketBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final EventAndTicketService eventAndTicketService;
    private final TicketBookingService ticketBookingService;

    @GetMapping("/v1/client/event")
    public ResponseEntity<List<EventResponse>> getOnGoingEvents() {
        return ResponseEntity.ok(eventAndTicketService.getEvents());
    }

    @GetMapping("/v1/client/event/{eventId}")
    public ResponseEntity<List<TicketResponse>> getAvailableTickets(@PathVariable("eventId") long eventId) {
        return ResponseEntity.ok(eventAndTicketService.getAvailableTickets(eventId));
    }

    @PostMapping("/v1/client/event/{eventId}/ticket/{ticketId}/{accountId}")
    public ResponseEntity<HoldTicketResponse> holdATicket(@PathVariable("eventId") long eventId,
                                                          @PathVariable("ticketId") long ticketId,
                                                          @PathVariable("accountId") long accountId) {
        return ResponseEntity.ok(eventAndTicketService.holdATicket(eventId, ticketId, accountId));
    }

    @GetMapping(value = "/v1/client/booking/{bookingId}", produces = "application/json")
    public ResponseEntity<String> findBookingStatus(@PathVariable("bookingId") long bookingId) {
        Optional<TicketBookingHistory> historyOptional = ticketBookingService.findById(bookingId);
        if (historyOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("booking id does not exist.");
        }

        TicketBookingHistory ticketBookingHistory = historyOptional.get();
        if (ticketBookingHistory.getBookingStatus().equals(BookingStatus.FAILED)) {
            return ResponseEntity.ok().body("{\n" +
                    "    \"status\": \"Failed to book\"\n" +
                    "}");
        }
        if (ticketBookingHistory.getBookingStatus().equals(BookingStatus.BOOKED)) {
            return ResponseEntity.ok().body("{\n" +
                    "    \"status\": \"Success to book\"\n" +
                    "}");
        }
        return ResponseEntity.ok().body(ticketBookingHistory.getHoldResponse());
    }

}
