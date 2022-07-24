package com.shah.bankingapplicationcrud.service;

import com.shah.bankingapplicationcrud.model.GetAllCustomerResponse;
import com.shah.bankingapplicationcrud.model.GetOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    GetOneCustomerResponse getOneEmployee(GetOneCustomerRequest request, HttpHeaders headers);
    GetAllCustomerResponse getAllEmployees(HttpHeaders headers);
}