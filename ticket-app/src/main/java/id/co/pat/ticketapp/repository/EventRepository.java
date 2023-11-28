package id.co.pat.ticketapp.repository;


import id.co.pat.ticketapp.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "events", path= "events")
public interface EventRepository extends CrudRepository<Event, Long> {

}
