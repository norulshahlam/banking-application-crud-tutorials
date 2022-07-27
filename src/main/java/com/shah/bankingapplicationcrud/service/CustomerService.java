package com.shah.bankingapplicationcrud.service;

import com.shah.bankingapplicationcrud.model.request.CreateCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.PatchCustomerRequest;
import com.shah.bankingapplicationcrud.model.response.CreateOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.DeleteOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.GetAllCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    GetOneCustomerResponse getOneCustomer(GetOneCustomerRequest request, HttpHeaders headers);

    GetAllCustomerResponse getAllCustomers(HttpHeaders headers);

    CreateOneCustomerResponse createOneCustomer(CreateCustomerRequest createCustomerRequest, HttpHeaders headers);

    CreateOneCustomerResponse patchOneCustomer(PatchCustomerRequest createCustomerRequest, HttpHeaders headers);

    DeleteOneCustomerResponse deleteOneCustomer(GetOneCustomerRequest request, HttpHeaders headers);
}