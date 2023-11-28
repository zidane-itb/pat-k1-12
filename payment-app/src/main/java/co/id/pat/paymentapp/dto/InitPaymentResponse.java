package co.id.pat.paymentapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class InitPaymentResponse {

    @JsonProperty("invoice-number")
    private long invoiceNumber;
    @JsonProperty("payment-url")
    private String paymentUrl;
}
