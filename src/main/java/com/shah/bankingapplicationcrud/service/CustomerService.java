package com.shah.bankingapplicationcrud.service;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.*;
import com.shah.bankingapplicationcrud.model.response.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.util.UUID;


public interface CustomerService {

    BankingResponse<Customer> getOneCustomer(GetOneCustomerRequest request, HttpHeaders headers);

    BankingResponse<Customer> createOneCustomer(CreateCustomerRequest createCustomerRequest, HttpHeaders headers);

    BankingResponse<Customer> updateOneCustomer(PatchCustomerRequest createCustomerRequest, HttpHeaders headers);

    BankingResponse<UUID> deleteOneCustomer(GetOneCustomerRequest request, HttpHeaders headers);

    BankingResponse<Page<Customer>> getAllCustomersOrSearchByLastAndFirstName(HttpHeaders headers, String name, int page, int size, String field);

    BankingResponse<TransferResponseDto> transferAmount(TransferRequest request, HttpHeaders headers);
}