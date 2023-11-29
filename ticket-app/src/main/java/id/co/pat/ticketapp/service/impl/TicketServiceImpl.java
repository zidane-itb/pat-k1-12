package id.co.pat.ticketapp.service.impl;

import id.co.pat.ticketapp.dto.HoldTicketResponse;
import id.co.pat.ticketapp.dto.InitPaymentRequest;
import id.co.pat.ticketapp.dto.InitPaymentResponse;
import id.co.pat.ticketapp.dto.TicketResponse;
import id.co.pat.ticketapp.model.Invoice;
import id.co.pat.ticketapp.model.Queue;
import id.co.pat.ticketapp.model.Ticket;
import id.co.pat.ticketapp.model.enums.InvoiceStatus;
import id.co.pat.ticketapp.model.enums.QueueStatus;
import id.co.pat.ticketapp.model.enums.TicketStatus;
import id.co.pat.ticketapp.repository.InvoiceRepository;
import id.co.pat.ticketapp.repository.QueueRepository;
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
    private final QueueRepository queueRepository;
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
        if (ticket.getTicketStatus().equals(TicketStatus.BOOKED)) {
            return null;
        }

        if (ticket.getTicketStatus().equals(TicketStatus.ON_GOING)) {
            queueRepository.save(Queue.builder()
                            .ticketId(ticketId)
                            .queueStatus(QueueStatus.WAITING)
                    .build());
            return HoldTicketResponse.builder()
                    .status("queued")
                    .build();
        }

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
                .status("created")
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

    @Override
    public HoldTicketResponse handleFailedInvoice(long ticketId, long invoiceNumber) {
        Optional<Queue> prevQOptional = queueRepository.findFirstByTicketIdAndQueueStatus(ticketId, QueueStatus.ONGOING);
        if (prevQOptional.isPresent()) {
            Queue q = prevQOptional.get();
            q.setQueueStatus(QueueStatus.FAILED);
            queueRepository.save(q);
        }

        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        Optional<Invoice> invoiceOptional = invoiceRepository.findInvoiceByInvoiceNumber(invoiceNumber);
        if (invoiceOptional.isEmpty() || ticketOptional.isEmpty()) {
            throw new RuntimeException();
        }
        Ticket ticket = ticketOptional.get(); Invoice prevInvoice = invoiceOptional.get();
        prevInvoice.setInvoiceStatus(InvoiceStatus.FAILED);
        invoiceRepository.save(prevInvoice);

        Optional<Queue> currentQOptional = queueRepository
                .findFirstByTicketIdAndQueueStatus(ticketId, QueueStatus.WAITING);
        if (currentQOptional.isEmpty()) {
            ticket.setTicketStatus(TicketStatus.OPEN);
            ticketRepository.save(ticket);
            log.info("current q empty with ticket id: {}", ticketId);
            return null;
        }

        Queue currentQ = currentQOptional.get();
        currentQ.setQueueStatus(QueueStatus.ONGOING);

        InitPaymentResponse response = feignPayment.initPayment(InitPaymentRequest.builder()
                .amount(ticket.getPrice())
                .build());

        assert response != null;
        log.info("created payment response from queue with id: {}, and url: {}", response.getInvoiceNumber(),
                response.getPaymentUrl());

        Invoice invoice = Invoice.builder()
                .invoiceStatus(InvoiceStatus.WAITING)
                .ticketId(ticketId)
                .invoiceNumber(response.getInvoiceNumber())
                .build();
        invoiceRepository.save(invoice);

        return HoldTicketResponse.builder()
                .invoiceNumber(response.getInvoiceNumber())
                .paymentUrl(response.getPaymentUrl())
                .status("created")
                .build();
    }

    @Override
    public Optional<Ticket> findTicketById(long ticketId) {
        return ticketRepository.findById(ticketId);
    }

}
