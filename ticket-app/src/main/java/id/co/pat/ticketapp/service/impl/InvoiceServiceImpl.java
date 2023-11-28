package id.co.pat.ticketapp.service.impl;

import id.co.pat.ticketapp.model.Invoice;
import id.co.pat.ticketapp.model.enums.InvoiceStatus;
import id.co.pat.ticketapp.repository.InvoiceRepository;
import id.co.pat.ticketapp.service.InvoiceService;
import id.co.pat.ticketapp.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final TicketService ticketService;

    @Override
    public void createInvoice(Long ticketId) {
        if (!ticketService.checkTicketExists(ticketId)) {
            log.info("Error while creating an invoice, ticket with id: {} does not exist",  ticketId);
            return;
        }
        Invoice invoice = Invoice.builder()
                .ticketId(ticketId)
                .invoiceStatus(InvoiceStatus.WAITING)
                .build();
        invoiceRepository.save(invoice);
    }

    @Override
    public void updateInvoiceStatus(Long invoiceNumber, InvoiceStatus invoiceStatus) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findInvoiceByInvoiceNumber(invoiceNumber);
        if (invoiceOptional.isEmpty()) {
            log.info("Error while creating an invoice, invoice with number: {} does not exist", invoiceNumber);
            return;
        }

        Invoice invoice = invoiceOptional.get();
        invoice.setInvoiceStatus(invoiceStatus);
        invoiceRepository.save(invoice);
    }

    @Override
    public boolean isInvoiceExists(Long invoiceNumber) {
        return invoiceRepository.existsByInvoiceNumber(invoiceNumber);
    }

    @Override
    public Optional<Invoice> getInvoice(Long invoiceId) {
        return invoiceRepository.findInvoiceByInvoiceNumber(invoiceId);
    }

}
