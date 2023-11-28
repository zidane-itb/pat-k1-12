package co.id.pat.paymentapp.controller;

import co.id.pat.paymentapp.dto.InitPaymentRequest;
import co.id.pat.paymentapp.dto.InitPaymentResponse;
import co.id.pat.paymentapp.dto.PaymentStatusResponse;
import co.id.pat.paymentapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final PaymentService paymentService;

    @PostMapping("/v1/payment")
    public ResponseEntity<InitPaymentResponse> initPayment(@RequestBody InitPaymentRequest initPaymentRequest) {
        double amount = initPaymentRequest.getAmount();
        InitPaymentResponse paymentResponse = paymentService.createPayment(amount);
        log.info("created payment response with id: {}, and url: {}", paymentResponse.getInvoiceNumber(),
                paymentResponse.getPaymentUrl());
        return ResponseEntity.ok(paymentResponse);
    }

    @PostMapping("/v1/payment/{invoiceId}/_pay")
    public ResponseEntity<Boolean> payInvoice(@PathVariable(name = "invoiceId") Long invoiceId) {
        boolean success = paymentService.pay(invoiceId);
        int statusCode = success ? 200:500;
        return ResponseEntity.status(statusCode)
                .body(success);
    }

    @GetMapping("/v1/payment/{invoiceId}")
    public ResponseEntity<PaymentStatusResponse> getInvoiceStatus(@PathVariable(name = "invoiceId") Long invoiceId) {
        return ResponseEntity.ok(paymentService.checkPaymentStatus(invoiceId));
    }

}
