package id.co.pat.ticketapp.dto;

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
public class HoldTicketResponse {

    @JsonProperty("status")
    private String status;
    @JsonProperty("invoice-number")
    private Long invoiceNumber;
    @JsonProperty("payment-url")
    private String paymentUrl;

}
