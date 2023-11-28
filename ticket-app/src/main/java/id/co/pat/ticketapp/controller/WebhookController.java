package id.co.pat.ticketapp.controller;

import id.co.pat.ticketapp.dto.HoldTicketResponse;
import id.co.pat.ticketapp.dto.KafkaBookingStatus;
import id.co.pat.ticketapp.dto.PaymentWebhookRequest;
import id.co.pat.ticketapp.model.Invoice;
import id.co.pat.ticketapp.model.enums.InvoiceStatus;
import id.co.pat.ticketapp.model.enums.TicketStatus;
import id.co.pat.ticketapp.service.InvoiceService;
import id.co.pat.ticketapp.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final KafkaTemplate<String, KafkaBookingStatus> kafkaTemplate;
    private final InvoiceService invoiceService;
    private final TicketService ticketService;

    @Value(value = "${app.config.kafka.booking-status-topic}")
    private String bookingTopic;

    @PostMapping("/v1/ticket/status-webhook")
    public ResponseEntity<Boolean> postBookingStatus(@RequestBody PaymentWebhookRequest request) {
        Long invoiceId = request.getInvoiceId();
        boolean success = request.isSuccess();

        log.info("get booking status webhook request, with invoiceId: {} and status: {}", invoiceId, success);
        if (!invoiceService.isInvoiceExists(invoiceId)) {
            log.error("Error while updating invoice: invoice id does not exist with invoice id: {}", invoiceId);
            return ResponseEntity.badRequest().body(false);
        }

        Invoice invoice = invoiceService.getInvoice(invoiceId).get();
        InvoiceStatus newInvoiceStatus = success ? InvoiceStatus.PAID : InvoiceStatus.FAILED;

        Long ticketId = invoice.getTicketId();
        invoiceService.updateInvoiceStatus(invoiceId, newInvoiceStatus);

        HoldTicketResponse newInvoice = null;
        if (success) {
            ticketService.updateTicketStatus(ticketId, TicketStatus.BOOKED);
        } else {
            newInvoice = ticketService.handleFailedInvoice(ticketId, invoiceId);
        }

        KafkaBookingStatus kafkaBookingStatus = KafkaBookingStatus.builder()
                .ticketId(ticketId)
                .success(success)
                .invoiceFileName("placeholder.txt")
                .newInvoice(newInvoice)
                .build();

        kafkaTemplate.send(bookingTopic, kafkaBookingStatus);
        return ResponseEntity.ok().body(true);
    }

}
