package co.id.pat.paymentapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PaymentAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentAppApplication.class, args);
	}

}
