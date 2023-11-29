package id.co.pat.ticketapp.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import id.co.pat.ticketapp.dto.HoldTicketResponse;
import id.co.pat.ticketapp.dto.KafkaBookingStatus;
import id.co.pat.ticketapp.dto.PaymentWebhookRequest;
import id.co.pat.ticketapp.model.Invoice;
import id.co.pat.ticketapp.model.Ticket;
import id.co.pat.ticketapp.model.enums.InvoiceStatus;
import id.co.pat.ticketapp.model.enums.TicketStatus;
import id.co.pat.ticketapp.service.FtpService;
import id.co.pat.ticketapp.service.InvoiceService;
import id.co.pat.ticketapp.service.TicketService;
import id.co.pat.ticketapp.util.PdfUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final KafkaTemplate<String, KafkaBookingStatus> kafkaTemplate;
    private final InvoiceService invoiceService;
    private final TicketService ticketService;
    private final FtpService ftpService;

    @Value(value = "${app.config.kafka.booking-status-topic}")
    private String bookingTopic;

    private static final Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

    static {
        cfg.setClassForTemplateLoading(WebhookController.class, "/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    @PostMapping("/v1/ticket/status-webhook")
    @Transactional
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
        String invoiceFileName = null;
        if (success) {
            ticketService.updateTicketStatus(ticketId, TicketStatus.BOOKED);
            Ticket ticket = ticketService.findTicketById(ticketId).get();

            try {
                String html = constructHistoryHtml(ticketId, ticket.getSeatId(), ticket.getPrice());
                byte[] pdf = PdfUtil.writePdfToFile(html);
                ftpService.uploadFile(pdf, "TICKET-"+ticketId);
                invoiceFileName = "TICKET-"+ticketId;
            } catch (TemplateException | IOException e) {
                log.error("Error while uploading to ftp with err: {}", e);
            }

        } else {
            newInvoice = ticketService.handleFailedInvoice(ticketId, invoiceId);
        }

        KafkaBookingStatus kafkaBookingStatus = KafkaBookingStatus.builder()
                .ticketId(ticketId)
                .success(success)
                .invoiceFileName(invoiceFileName)
                .newInvoice(newInvoice)
                .build();

        kafkaTemplate.send(bookingTopic, kafkaBookingStatus);
        return ResponseEntity.ok().body(true);
    }

    private String constructHistoryHtml(Long ticketId, int seatNumber, double price) throws IOException, TemplateException {
        Template template = cfg.getTemplate("ticket.ftl");
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("ticketId", ticketId);
        dataModel.put("seatNumber", seatNumber);
        dataModel.put("price", price);

        StringWriter stringWriter = new StringWriter();
        template.process(dataModel, stringWriter);
        return stringWriter.toString();
    }

}
