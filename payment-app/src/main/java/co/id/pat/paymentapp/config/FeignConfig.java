package co.id.pat.paymentapp.config;

import org.springframework.context.annotation.Configuration;
import feign.Retryer;
import org.springframework.context.annotation.Bean;


@Configuration
public class FeignConfig {

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 500, 2);
    }

}
