package co.id.pat.clientapp;

import co.id.pat.clientapp.model.Account;
import co.id.pat.clientapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients
public class ClientAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientAppApplication.class, args);
	}

	@Autowired
	public void populate(AccountRepository accountRepository) {
		for (int i = 0; i < 10; ++i) {
			Account account = Account.builder()
					.firstName("Zidane")
					.lastName("Firzatullah")
					.email("zfirzatullah21@gmail.com")
					.build();

			accountRepository.save(account);
		}


	}

}
