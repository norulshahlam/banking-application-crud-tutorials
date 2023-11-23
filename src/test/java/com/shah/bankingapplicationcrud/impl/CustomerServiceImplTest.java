package com.shah.bankingapplicationcrud.impl;

import com.shah.bankingapplicationcrud.exception.CrudException;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.*;
import com.shah.bankingapplicationcrud.model.response.BankingResponse;
import com.shah.bankingapplicationcrud.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.RANDOM_UUID1;
import static com.shah.bankingapplicationcrud.constant.CommonConstants.RANDOM_UUID2;
import static com.shah.bankingapplicationcrud.exception.CrudErrorCodes.*;
import static com.shah.bankingapplicationcrud.model.enums.ResponseStatus.FAILURE;
import static com.shah.bankingapplicationcrud.model.enums.ResponseStatus.SUCCESS;
import static com.shah.bankingapplicationcrud.service.Initializer.*;
import static java.math.BigDecimal.valueOf;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository custRepo;

    @InjectMocks
    private CustomerServiceImpl service;

    private Customer customer = null;
    private HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void setUp() {
        openMocks(this);
        setField(service, "custRepo", custRepo);
        headers = initializeHeader();
        customer = initCustomers();
    }

    @Test
    void getAllCustomersOrSearchByLastAndFirstName_found() {
        Page<Customer> pagedCustomers = new PageImpl<>(List.of(customer));
        when(custRepo.findAll(
                any(Specification.class),
                any(Pageable.class)))
                .thenReturn(pagedCustomers);

        BankingResponse<Page<Customer>> response = service.getAllCustomersOrSearchByLastAndFirstName(headers, "bob", 1, 1, "email");

        assertThat(response.getData().get().findFirst()).contains(customer);
    }

    @Test
    void getAllCustomersOrSearchByLastAndFirstName_not_found() {
        when(custRepo.findAll(
                any(Specification.class),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(of()));

        BankingResponse<Page<Customer>> response = service.getAllCustomersOrSearchByLastAndFirstName(headers, "", 1, 1, "email");

        assertThat(response.getStatus()).isEqualTo(FAILURE);
        assertThat(response.getError().getErrorCode()).isEqualTo(CUSTOMER_NOT_FOUND.getCode());
    }

    @Test
    void getOneCustomer_customer_found() {
        when(custRepo.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().accountNumber(RANDOM_UUID1).build();
        BankingResponse<Customer> oneCustomer = service.getOneCustomer(request, headers);
        assertThat(oneCustomer).isNotNull();
        assertThat(oneCustomer.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void getOneCustomer_customer_NOT_found() {
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().accountNumber(RANDOM_UUID1).build();
        BankingResponse<Customer> response = service.getOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAILURE);
        assertThat(response.getError().getErrorCode()).isEqualTo(CUSTOMER_NOT_FOUND.getCode());
    }

    @Test
    void createOneCustomer_success() {

        CreateCustomerRequest request = CreateCustomerRequest.builder().build();
        copyProperties(customer, request);

        BankingResponse<Customer> response = service.createOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);

        //createOneCustomer_FAILED
        when(custRepo.save(any())).thenThrow(new CrudException(AC_BAD_REQUEST, AC_INTERNAL_SERVER_ERROR));
        response = service.createOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAILURE);
        assertThat(response.getError().getErrorCode()).isEqualTo(AC_INTERNAL_SERVER_ERROR.getCode());
    }

    @Test
    void updateOneCustomer_success() {
        PatchCustomerRequest request = PatchCustomerRequest.builder().build();
        copyProperties(customer, request);
        request.setAccountNumber(RANDOM_UUID1);
        when(custRepo.findById(any())).thenReturn(Optional.of(customer));
        BankingResponse<Customer> response = service.updateOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void updateOneCustomer_fail_CUSTOMER_NOT_FOUND() {
        PatchCustomerRequest request = PatchCustomerRequest.builder().build();
        copyProperties(customer, request);
        BankingResponse<Customer> response = service.updateOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAILURE);
        assertThat(response.getError().getErrorCode()).isEqualTo(CUSTOMER_NOT_FOUND.getCode());
    }

    @Test
    void deleteOneCustomer_success() {
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().accountNumber(RANDOM_UUID1).build();
        when(custRepo.findById(any())).thenReturn(Optional.of(customer));
        BankingResponse<UUID> response = service.deleteOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void deleteOneCustomer_fail_CUSTOMER_NOT_FOUND() {
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().accountNumber(RANDOM_UUID1).build();
        BankingResponse<UUID> response = service.deleteOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAILURE);
        assertThat(response.getError().getErrorCode()).isEqualTo(CUSTOMER_NOT_FOUND.getCode());
    }

    @Test
    void transferAmount() {
        TransferRequest request = initTransferAmount();

        //INITIALIZE PAYEE
        Customer payee = customer;

        //INITIALIZE PAYER
        Customer payer = customer;

        // SUCCESS
        when(custRepo.findById(RANDOM_UUID1)).thenReturn(Optional.of(payer));
        when(custRepo.findById(RANDOM_UUID2)).thenReturn(Optional.of(payee));
        when(custRepo.saveAll(of(payee, payer))).thenReturn(of(payee, payer));
        BankingResponse<TransferResponseDto> response = service.transferAmount(request, headers);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);

        // PAYEE NOT FOUND
        when(custRepo.findById(RANDOM_UUID2)).thenReturn(Optional.empty());
        response = service.transferAmount(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAILURE);
        assertThat(response.getError().getErrorCode()).isEqualTo(PAYEE_ACCOUNT_NOT_FOUND.getCode());

        // INSUFFICIENT BALANCE
        request.setAmount(valueOf(1000.50));
        response = service.transferAmount(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAILURE);
        assertThat(response.getError().getErrorCode()).isEqualTo(INSUFFICIENT_AMOUNT.getCode());

        // PAYER NOT FOUND
        when(custRepo.findById(RANDOM_UUID1)).thenReturn(Optional.empty());
        response = service.transferAmount(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAILURE);
        assertThat(response.getError().getErrorCode()).isEqualTo(PAYER_ACCOUNT_NOT_FOUND.getCode());
    }
}