package com.sssupply.customerportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CustomerportalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerportalApplication.class, args);
    }
}
