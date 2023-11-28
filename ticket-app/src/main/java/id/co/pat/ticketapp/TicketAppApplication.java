package id.co.pat.ticketapp;

import id.co.pat.ticketapp.model.Ticket;
import id.co.pat.ticketapp.model.enums.TicketStatus;
import id.co.pat.ticketapp.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TicketAppApplication {



	public static void main(String[] args) {
		SpringApplication.run(TicketAppApplication.class, args);
	}

	@Autowired
	public void populate(TicketRepository ticketRepository) {
		for (int i = 0; i < 50; ++i) {
			Ticket ticket1 = Ticket.builder()
					.eventId(1L)
					.seatId(1)
					.price(2000)
					.ticketStatus(TicketStatus.OPEN)
					.build();

			ticketRepository.save(ticket1);
		}
	}

}
