package co.id.pat.clientapp.service.impl.feign;

import co.id.pat.clientapp.dto.EventResponse;
import co.id.pat.clientapp.dto.HoldTicketResponse;
import co.id.pat.clientapp.dto.TicketResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "ticket-app", url = "http://ticket-app:8081")
@Component
public interface FeignTicket {

    @GetMapping("/v1/event")
    List<EventResponse> getEvents();
    @GetMapping("/v1/event/{eventId}")
    List<TicketResponse> getAvailableTickets(@PathVariable("eventId") long eventId);
    @PostMapping("/v1/event/{eventId}/ticket/{ticketId}")
    HoldTicketResponse holdATicket(@PathVariable("eventId") long eventId,
                                   @PathVariable("ticketId") long ticketId);

}
