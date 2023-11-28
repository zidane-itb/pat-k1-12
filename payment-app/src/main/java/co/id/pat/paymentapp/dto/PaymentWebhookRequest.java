package co.id.pat.paymentapp.dto;

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
public class PaymentWebhookRequest {
    @JsonProperty("invoice-id")
    private long invoiceId;
    @JsonProperty("success")
    private boolean success;
}
