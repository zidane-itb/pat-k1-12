package co.id.pat.paymentapp.service;

import co.id.pat.paymentapp.dto.InitPaymentResponse;
import co.id.pat.paymentapp.dto.PaymentStatusResponse;

public interface PaymentService {

    InitPaymentResponse createPayment(double amount);
    boolean pay(Long invoiceId);
    PaymentStatusResponse checkPaymentStatus(Long invoiceId);

}
