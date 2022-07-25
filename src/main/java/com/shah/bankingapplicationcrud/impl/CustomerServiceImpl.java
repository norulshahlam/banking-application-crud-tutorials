package com.shah.bankingapplicationcrud.impl;

import com.shah.bankingapplicationcrud.exception.CrudError;
import com.shah.bankingapplicationcrud.exception.CrudException;
import com.shah.bankingapplicationcrud.model.response.CreateOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.GetAllCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.dto.CustomerDto;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import com.shah.bankingapplicationcrud.repository.CustomerRepository;
import com.shah.bankingapplicationcrud.service.CustomerService;
import com.shah.bankingapplicationcrud.validation.ValidateHeaders;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.shah.bankingapplicationcrud.exception.CrudErrorCodes.*;

@Service
@Data
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private final CustomerRepository custRepo;

    @Override
    public GetOneCustomerResponse getOneCustomer(GetOneCustomerRequest request, HttpHeaders headers) {
        log.info("Getting one customer...");
        try {
            ValidateHeaders.validateGetOneEmployee(headers);
            Optional<Customer> customer = custRepo.findById(UUID.fromString(request.getId()));
            if (customer.isEmpty()) {
                throw new CrudException(AC_BUSINESS_ERROR, CUSTOMER_NOT_FOUND);
            }
            return GetOneCustomerResponse.success(customer.get());

        } catch (CrudException e) {
            return GetOneCustomerResponse.fail(null, CrudError.constructErrorForCrudException(e));
        }
    }

    @Override
    public GetAllCustomerResponse getAllCustomers(HttpHeaders headers) {
        log.info("Getting all customer...");
        try {
            ValidateHeaders.validateGetOneEmployee(headers);
            List<Customer> customers = custRepo.findAll();
            if (customers.isEmpty()) {
                throw new CrudException(AC_BUSINESS_ERROR, NO_CUSTOMER);
            }
            return GetAllCustomerResponse.success(customers);
        } catch (CrudException e) {
            return GetAllCustomerResponse.fail(null, CrudError.constructErrorForCrudException(e));
        }
    }

    @Override
    public CreateOneCustomerResponse createOneCustomer(CustomerDto customerDto, HttpHeaders headers) {
        try {
            Customer customer = new Customer();
            BeanUtils.copyProperties(customerDto, customer);
            return CreateOneCustomerResponse.success(custRepo.save(customer));

        } catch (CrudException e) {
            return CreateOneCustomerResponse.fail(null, CrudError.constructErrorForCrudException(e));
        }
    }
}
