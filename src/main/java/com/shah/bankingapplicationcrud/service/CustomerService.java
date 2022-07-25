package com.shah.bankingapplicationcrud.service;

import com.shah.bankingapplicationcrud.model.response.CreateOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.GetAllCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.dto.CustomerDto;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    GetOneCustomerResponse getOneCustomer(GetOneCustomerRequest request, HttpHeaders headers);
    GetAllCustomerResponse getAllCustomers(HttpHeaders headers);
    CreateOneCustomerResponse createOneCustomer(CustomerDto customerDto, HttpHeaders headers);
}