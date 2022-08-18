package com.shah.bankingapplicationcrud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@Slf4j
public class BankingApplicationCrudApplication {

    public static void main(String[] args) {
        run(BankingApplicationCrudApplication.class, args);
        log.info("App started");
    }
}
