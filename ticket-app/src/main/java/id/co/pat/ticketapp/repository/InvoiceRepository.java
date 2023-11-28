package id.co.pat.ticketapp.repository;

import id.co.pat.ticketapp.model.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

    Optional<Invoice> findInvoiceByInvoiceNumber(long invoiceNumber);
    boolean existsByInvoiceNumber(long invoiceNumber);

}
