package com.shah.bankingapplicationcrud.service;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import org.springframework.http.HttpHeaders;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static java.math.BigDecimal.valueOf;
import static java.util.UUID.fromString;

public class Initializer {

    private static Customer customer = null;
    private static HttpHeaders headers = new HttpHeaders();

    public static HttpHeaders initializeHeader() {
        headers.add(X_SOURCE_COUNTRY, SG);
        headers.add(X_CORRELATION_ID, RANDOM_UUID1);
        headers.add(X_SOURCE_DATE_TIME, LocalDate.now().toString());
        return headers;
    }

    public static Customer initCustomers() {
        return customer = Customer.builder()
                .email("norulshahlam@gmail.com")
                .firstName("norulshahlam")
                .lastName("bin mohsen")
                .gender("male")
                .age(21)
                .country("Singapore")
                .birthDate(new Date(2000 - 3 - 29))
                .accountNumber(fromString(RANDOM_UUID1))
                .accBalance(valueOf(10.50))
                .updatedAt(ZonedDateTime.now())
                .build();
    }

}
