package id.co.pat.ticketapp.service.impl;

import id.co.pat.ticketapp.dto.HoldTicketResponse;
import id.co.pat.ticketapp.dto.InitPaymentRequest;
import id.co.pat.ticketapp.dto.InitPaymentResponse;
import id.co.pat.ticketapp.dto.TicketResponse;
import id.co.pat.ticketapp.model.Invoice;
import id.co.pat.ticketapp.model.Ticket;
import id.co.pat.ticketapp.model.enums.InvoiceStatus;
import id.co.pat.ticketapp.model.enums.TicketStatus;
import id.co.pat.ticketapp.repository.InvoiceRepository;
import id.co.pat.ticketapp.repository.TicketRepository;
import id.co.pat.ticketapp.service.TicketService;
import id.co.pat.ticketapp.service.impl.feign.FeignPayment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final InvoiceRepository invoiceRepository;
    private final FeignPayment feignPayment;

    @Override
    public List<TicketResponse> getEventAvailableTickets(long eventId) {
        List<Ticket> tickets = ticketRepository.findAvailableTickets(eventId);
        return tickets.stream().map(ticket -> TicketResponse
                .builder().id(ticket.getId())
                .seatId(ticket.getSeatId())
                .price(ticket.getPrice())
                .build()).toList();
    }

    @Override
    @Transactional
    public HoldTicketResponse holdTicketAndGetInvoiceNumber(long ticketId) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (ticketOptional.isEmpty())
            return null;

        Ticket ticket = ticketOptional.get();
        ticket.setTicketStatus(TicketStatus.ON_GOING);
        ticketRepository.save(ticket);

        InitPaymentResponse response = feignPayment.initPayment(InitPaymentRequest.builder()
                        .amount(ticket.getPrice())
                .build());

        assert response != null;
        log.info("created payment response with id: {}, and url: {}", response.getInvoiceNumber(),
                response.getPaymentUrl());

        invoiceRepository.save(Invoice.builder()
                .invoiceStatus(InvoiceStatus.WAITING)
                .ticketId(ticketId)
                .invoiceNumber(response.getInvoiceNumber())
                .build());

        return HoldTicketResponse.builder()
                .invoiceNumber(response.getInvoiceNumber())
                .paymentUrl(response.getPaymentUrl())
                .build();
    }

    @Override
    public void updateTicketStatus(long ticketId, TicketStatus status) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (ticketOptional.isEmpty())
            return;

        Ticket ticket = ticketOptional.get();
        ticket.setTicketStatus(status);
        ticketRepository.save(ticket);
    }

    @Override
    public boolean checkTicketEligibility(long ticketId, long eventId) {
        return ticketRepository.existsByIdAndEventIdAndOpen(ticketId, eventId);
    }

    @Override
    public boolean checkTicketExists(long ticketId) {
        return ticketRepository.existsById(ticketId);
    }

}
