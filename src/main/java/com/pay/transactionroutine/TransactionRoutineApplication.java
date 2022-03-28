package com.pay.transactionroutine;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@OpenAPIDefinition(info = @Info(title = "Transaction routine", version = "2.0", description = "Transaction routine information "))
public class TransactionRoutineApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionRoutineApplication.class, args);
	}

}
