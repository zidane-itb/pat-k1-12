package id.co.pat.ticketapp.service.impl.feign;

import id.co.pat.ticketapp.dto.InitPaymentRequest;
import id.co.pat.ticketapp.dto.InitPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-app", url = "http://payment-app:8082")
@Component
public interface FeignPayment {

    @PostMapping("/v1/payment")
    InitPaymentResponse initPayment(@RequestBody InitPaymentRequest initPaymentRequest);

}
