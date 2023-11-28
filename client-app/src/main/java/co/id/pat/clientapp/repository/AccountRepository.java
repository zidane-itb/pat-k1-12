package co.id.pat.clientapp.repository;

import co.id.pat.clientapp.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * This class is automatically exposed the rest endpoints to do CRUD functionality
 */
@RepositoryRestResource(collectionResourceRel = "account",path= "account")
public interface AccountRepository extends CrudRepository<Account, Long> {
}
