package id.co.pat.ticketapp.service;

import id.co.pat.ticketapp.model.Invoice;
import id.co.pat.ticketapp.model.enums.InvoiceStatus;

import java.util.Optional;

public interface InvoiceService {

    void createInvoice(Long ticketId);
    void updateInvoiceStatus(Long invoiceId, InvoiceStatus invoiceStatus);
    boolean isInvoiceExists(Long invoiceId);
    Optional<Invoice> getInvoice(Long invoiceId);
}
