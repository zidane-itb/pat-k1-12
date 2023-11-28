package co.id.pat.paymentapp.dto;

import co.id.pat.paymentapp.model.enums.InvoiceStatus;
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
public class PaymentStatusResponse {

    @JsonProperty("status")
    private InvoiceStatus status;
    @JsonProperty("annotation")
    private String annotation;
}
