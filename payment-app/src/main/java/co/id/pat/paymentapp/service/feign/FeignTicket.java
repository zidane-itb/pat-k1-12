package co.id.pat.paymentapp.service.feign;

import co.id.pat.paymentapp.dto.PaymentWebhookRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ticket-app", url = "http://localhost:8081")
@Component
public interface FeignTicket {

    @PostMapping("/v1/ticket/status-webhook")
    void postPaymentStatus(@RequestBody PaymentWebhookRequest paymentWebhookRequest);

}
