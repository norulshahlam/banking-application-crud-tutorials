package com.shah.bankingapplicationcrud.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyStartup implements CommandLineRunner {

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void run(String... args) {
        log.info("Application name: {} just loaded!", appName);
        log.info("EXECUTING : command line runner");

        for (int i = 0; i < args.length; ++i) {
            log.info("args[{}]: {}", i, args[i]);
        }
    }
}
