package co.id.pat.paymentapp.service.impl;

import co.id.pat.paymentapp.dto.InitPaymentResponse;
import co.id.pat.paymentapp.dto.PaymentStatusResponse;
import co.id.pat.paymentapp.dto.PaymentWebhookRequest;
import co.id.pat.paymentapp.exception.BadRequestException;
import co.id.pat.paymentapp.exception.InternalErrorException;
import co.id.pat.paymentapp.model.Invoice;
import co.id.pat.paymentapp.model.enums.InvoiceStatus;
import co.id.pat.paymentapp.repository.InvoiceRepository;
import co.id.pat.paymentapp.service.PaymentService;
import co.id.pat.paymentapp.service.feign.FeignTicket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final InvoiceRepository invoiceRepository;
    private final FeignTicket feignTicket;

    @Override
    public InitPaymentResponse createPayment(double amount) {
        Invoice invoice = Invoice.builder()
                .amount(amount)
                .invoiceStatus(InvoiceStatus.WAITING)
                .build();

        invoiceRepository.save(invoice);
        return InitPaymentResponse.builder()
                .invoiceNumber(invoice.getId())
                .paymentUrl(String.format("/v1/payment/%s/_pay", invoice.getId()))
                .build();
    }

    @Override
    @Transactional
    public boolean pay(Long invoiceId) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
        if (invoiceOptional.isEmpty()) {
            throw new BadRequestException("Invoice not found");
        }
        Invoice invoice = invoiceOptional.get();
        if (invoice.getInvoiceStatus().equals(InvoiceStatus.PAID)) {
            throw new BadRequestException("Payment already paid");
        }
        if (invoice.getInvoiceStatus().equals(InvoiceStatus.FAILED)) {
            throw new BadRequestException("Payment failed");
        }

        double probability = 0.1;
        double randomValue = Math.random();
        boolean success = true;
        if (randomValue < probability) {
            invoice.setInvoiceStatus(InvoiceStatus.FAILED);
            invoiceRepository.save(invoice);
            success = false;
        }

        invoice.setInvoiceStatus(InvoiceStatus.PAID);
        invoiceRepository.save(invoice);

        try {
            feignTicket.postPaymentStatus(PaymentWebhookRequest.builder()
                    .invoiceId(invoiceId)
                    .success(success)
                    .build());
        } catch (Exception ex) {
            log.error("Error while calling feign post payment status with exception: {}", ex);
            throw new InternalErrorException();
        }
        return success;
    }

    @Override
    public PaymentStatusResponse checkPaymentStatus(Long invoiceId) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
        if (invoiceOptional.isEmpty()) {
            throw new BadRequestException("Invoice not found");
        }
        Invoice invoice = invoiceOptional.get();

        return PaymentStatusResponse.builder()
                .status(invoice.getInvoiceStatus())
                .build();
    }
}
