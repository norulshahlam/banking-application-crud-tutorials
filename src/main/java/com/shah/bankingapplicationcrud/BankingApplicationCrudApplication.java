package com.shah.bankingapplicationcrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

import static java.util.TimeZone.getTimeZone;
import static java.util.TimeZone.setDefault;
import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class BankingApplicationCrudApplication {

	public static void main(String[] args) {
		run(BankingApplicationCrudApplication.class, args);
	}
}
