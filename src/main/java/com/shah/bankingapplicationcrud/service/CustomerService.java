package com.shah.bankingapplicationcrud.service;

import com.shah.bankingapplicationcrud.model.request.CreateCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.PatchCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.TransferRequest;
import com.shah.bankingapplicationcrud.model.response.*;
import org.springframework.http.HttpHeaders;


public interface CustomerService {

    GetOneCustomerResponse getOneCustomer(GetOneCustomerRequest request, HttpHeaders headers);

    CreateOneCustomerResponse createOneCustomer(CreateCustomerRequest createCustomerRequest, HttpHeaders headers);

    CreateOneCustomerResponse updateOneCustomer(PatchCustomerRequest createCustomerRequest, HttpHeaders headers);

    DeleteOneCustomerResponse deleteOneCustomer(GetOneCustomerRequest request, HttpHeaders headers);

    SearchCustomerResponse getAllCustomersOrSearchByLastAndFirstName(HttpHeaders headers, String name, int page, int size, String field);

    TransferAmountResponse transferAmount(TransferRequest request, HttpHeaders headers);
}