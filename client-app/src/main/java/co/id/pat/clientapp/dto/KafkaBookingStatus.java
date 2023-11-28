package co.id.pat.clientapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Data
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
