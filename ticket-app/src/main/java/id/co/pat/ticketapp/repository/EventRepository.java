package id.co.pat.ticketapp.repository;


import id.co.pat.ticketapp.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {

}
