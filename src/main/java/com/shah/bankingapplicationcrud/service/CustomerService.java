package com.shah.bankingapplicationcrud.service;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.*;
import com.shah.bankingapplicationcrud.model.response.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.util.UUID;


public interface CustomerService {

    MyResponse<Customer> getOneCustomer(UUID request);

    MyResponse<Customer> createOneCustomer(CreateCustomerRequest createCustomerRequest);

    MyResponse<Customer> updateOneCustomer(PatchCustomerRequest createCustomerRequest);

    MyResponse<UUID> deleteOneCustomer(GetOneCustomerRequest request);

    MyResponse<Page<Customer>> getAllCustomersOrSearchByLastAndFirstName(HttpHeaders headers, String name, int page, int size, String field);

    MyResponse<TransferResponseDto> transferAmount(TransferRequest request);
}