package com.shah.bankingapplicationcrud.service;

import com.github.javafaker.Faker;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.TransferRequest;
import com.shah.bankingapplicationcrud.model.request.TransferResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static java.math.BigDecimal.valueOf;
import static java.time.ZonedDateTime.now;

public class Initializer {

    private static final Faker faker = new Faker();

    private Initializer() {
    }

    public static HttpHeaders initializeHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_SOURCE_COUNTRY, SG);
        headers.add(X_CORRELATION_ID, RANDOM_UUID1.toString());
        headers.add(X_SOURCE_DATE_TIME, LocalDate.now().toString());
        return headers;
    }

    public static Customer initCustomers() {
        return Customer.builder()
                .email(faker.internet().emailAddress())
                .firstName("norulshahlam")
                .lastName("bin mohsen")
                .gender("Male")
                .age(21)
                .country("Singapore")
                .birthDate(new Date(2000 - 3L - 29))
                .accountNumber(RANDOM_UUID1)
                .accBalance(valueOf(10.50))
                .updatedAt(now())
                .build();
    }

    public static MultiValueMap<String, String> initSearchCustomerParams() {
        MultiValueMap<String, String> searchCustomerParams = new LinkedMultiValueMap<>();
        searchCustomerParams.add("page", "0");
        searchCustomerParams.add("size", "20");
        searchCustomerParams.add("field", "email");
        searchCustomerParams.add("query", "Dorothy");
        return searchCustomerParams;
    }

    public static TransferRequest initTransferAmount() {
        return TransferRequest.builder()
                .payerAccountNumber(RANDOM_UUID1)
                .payeeAccountNumber(RANDOM_UUID2)
                .amount(valueOf(1.50))
                .build();
    }

    public static TransferResponseDto initTransferResponseDto() {
        return TransferResponseDto.builder()
                .payeeAccountNumber(RANDOM_UUID1)
                .payerFirstName("Adam")
                .payerOldAccBal(valueOf(11.50))
                .payerNewAccBal(valueOf(1.50))
                .payeeAccountNumber(RANDOM_UUID1)
                .payeeFirstName("Bob")
                .payeeOldAccBal(valueOf(1.50))
                .payeeNewAccBal(valueOf(1.50))
                .transactionDate(ZonedDateTime.now())
                .amountTransferred(valueOf(1.50))
                .build();
    }

}
