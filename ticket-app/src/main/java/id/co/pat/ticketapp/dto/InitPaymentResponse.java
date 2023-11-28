package id.co.pat.ticketapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class InitPaymentResponse {

    @JsonProperty("invoice-number")
    private long invoiceNumber;
    @JsonProperty("payment-url")
    private String paymentUrl;
}
