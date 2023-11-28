package id.co.pat.ticketapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class KafkaBookingStatus {

    @JsonProperty("ticket-id")
    private Long ticketId;
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("invoice-file-name")
    private String invoiceFileName;
    @JsonProperty("new-invoice")
    private HoldTicketResponse newInvoice;
}
