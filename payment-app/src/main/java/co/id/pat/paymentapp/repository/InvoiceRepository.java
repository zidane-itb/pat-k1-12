package co.id.pat.paymentapp.repository;

import co.id.pat.paymentapp.model.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
}
