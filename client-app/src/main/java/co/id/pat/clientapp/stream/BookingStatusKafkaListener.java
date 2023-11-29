package co.id.pat.clientapp.stream;

import co.id.pat.clientapp.dto.KafkaBookingStatus;
import co.id.pat.clientapp.model.TicketBookingHistory;
import co.id.pat.clientapp.model.enums.BookingStatus;
import co.id.pat.clientapp.service.FtpService;
import co.id.pat.clientapp.service.TicketBookingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingStatusKafkaListener {

    private final TicketBookingService ticketBookingService;
    private final FtpService ftpService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${app.config.kafka.booking-status-topic}",
            groupId = "${app.config.kafka.consumer-group}")
    public void listenGroupFoo(KafkaBookingStatus kafkaBookingStatus,  Acknowledgment ack) {
        log.info("Received Message in group: {}", kafkaBookingStatus);
        Long ticketId = kafkaBookingStatus.getTicketId();

        Optional<TicketBookingHistory> bookingHistoryO = ticketBookingService
                .getTicketBookingHistoryByTicketId(ticketId);
        if (bookingHistoryO.isEmpty()) {
            log.info("Booking history by ticket id with ticket id: {} is empty", ticketId);
            return;
        }
        TicketBookingHistory ticketBookingHistory = bookingHistoryO.get();

        if (kafkaBookingStatus.isSuccess()) {
            ticketBookingHistory.setBookingStatus(BookingStatus.BOOKED);
            ticketBookingHistory.setPdfName(kafkaBookingStatus.getInvoiceFileName());
            ticketBookingService.updateTicketBooking(ticketBookingHistory);
            ticketBookingService.invalidateAllWaiting(ticketId, kafkaBookingStatus.getTicketId());

            try {
                ftpService.downloadFile(kafkaBookingStatus.getInvoiceFileName(),
                        kafkaBookingStatus.getInvoiceFileName());
            } catch (IOException e) {
                log.error("Error while downloading pdf with filename: {} with err",
                        kafkaBookingStatus.getInvoiceFileName(), e);
            }
            ack.acknowledge();
            return;
        }

        ticketBookingHistory.setBookingStatus(BookingStatus.FAILED);
        ticketBookingService.updateTicketBooking(ticketBookingHistory);
        Optional<TicketBookingHistory> nextOptional = ticketBookingService.findNextInLine(ticketId,
                kafkaBookingStatus.getTicketId());

        if (nextOptional.isEmpty()) {
            log.error("There is no next booking with ticket id: {}", ticketId);
            return;
        }

        TicketBookingHistory next = nextOptional.get();
        next.setBookingStatus(BookingStatus.WAITING);
        try {
            next.setHoldResponse(objectMapper.writeValueAsString(kafkaBookingStatus.getNewInvoice()));
        } catch (JsonProcessingException e) {
            log.error("Error while writing new invoice in kafka: {}", e);
        }
        ticketBookingService.updateTicketBooking(next);

        ack.acknowledge();
    }

}
