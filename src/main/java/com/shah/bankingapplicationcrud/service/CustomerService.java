package com.shah.bankingapplicationcrud.service;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.*;
import com.shah.bankingapplicationcrud.model.response.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.util.UUID;


public interface CustomerService {

    BankingResponse<Customer> getOneCustomer(UUID request);

    BankingResponse<Customer> createOneCustomer(CreateCustomerRequest createCustomerRequest);

    BankingResponse<Customer> updateOneCustomer(PatchCustomerRequest createCustomerRequest);

    BankingResponse<UUID> deleteOneCustomer(GetOneCustomerRequest request);

    BankingResponse<Page<Customer>> getAllCustomersOrSearchByLastAndFirstName(HttpHeaders headers, String name, int page, int size, String field);

    BankingResponse<TransferResponseDto> transferAmount(TransferRequest request);
}