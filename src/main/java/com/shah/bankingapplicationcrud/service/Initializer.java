package com.shah.bankingapplicationcrud.service;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.TransferRequest;
import com.shah.bankingapplicationcrud.model.response.TransferResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static com.shah.bankingapplicationcrud.model.entity.Customer.builder;
import static java.math.BigDecimal.valueOf;
import static java.time.ZonedDateTime.now;
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
        return customer = builder()
                .email("norulshahlam@gmail.com")
                .firstName("norulshahlam")
                .lastName("bin mohsen")
                .gender("Male")
                .age(21)
                .country("Singapore")
                .birthDate(new Date(2000 - 3 - 29))
                .accountNumber(fromString(RANDOM_UUID1))
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
                .amount(valueOf(1.50))
                .build();
    }

}
