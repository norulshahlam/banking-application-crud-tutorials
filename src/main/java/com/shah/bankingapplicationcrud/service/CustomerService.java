package com.shah.bankingapplicationcrud.service;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.*;
import com.shah.bankingapplicationcrud.model.response.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.util.UUID;


public interface CustomerService {

    CustomerResponse<Customer> getOneCustomer(GetOneCustomerRequest request, HttpHeaders headers);

    CustomerResponse<Customer> createOneCustomer(CreateCustomerRequest createCustomerRequest, HttpHeaders headers);

    CustomerResponse<Customer> updateOneCustomer(PatchCustomerRequest createCustomerRequest, HttpHeaders headers);

    CustomerResponse<UUID> deleteOneCustomer(GetOneCustomerRequest request, HttpHeaders headers);

    CustomerResponse<Page<Customer>> getAllCustomersOrSearchByLastAndFirstName(HttpHeaders headers, String name, int page, int size, String field);

    CustomerResponse<TransferResponseDto> transferAmount(TransferRequest request, HttpHeaders headers);
}