package com.shah.bankingapplicationcrud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import static org.springframework.boot.SpringApplication.run;

/**
 * @author NORUL
 */
@SpringBootApplication
@Slf4j
@ConfigurationPropertiesScan
public class Application {

    public static void main(String[] args) {
        run(Application.class, args);
        log.info("App started");
    }
}
