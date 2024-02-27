package com.shah.bankingapplicationcrud.impl;

import com.shah.bankingapplicationcrud.constant.ErrorConstants;
import com.shah.bankingapplicationcrud.exception.MyException;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.*;
import com.shah.bankingapplicationcrud.model.response.MyResponse;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.RANDOM_UUID1;
import static com.shah.bankingapplicationcrud.constant.ErrorConstants.*;
import static com.shah.bankingapplicationcrud.model.enums.ResponseStatus.SUCCESS;
import static com.shah.bankingapplicationcrud.service.Initializer.*;
import static java.math.BigDecimal.valueOf;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.beans.BeanUtils.copyProperties;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository custRepo;

    @InjectMocks
    private CustomerServiceImpl service;

    private Customer customer = null;
    private HttpHeaders headers = new HttpHeaders();
    private MyException myException = null;

    @BeforeEach
    void setUp() {
        openMocks(this);
        ReflectionTestUtils.setField(service, "repository", custRepo);
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

        MyResponse<Page<Customer>> response = service.getAllCustomersOrSearchByLastAndFirstName(headers, "bob", 1, 1, "email");

        assertThat(response.getData().get().findFirst()).contains(customer);
    }

    @Test
    void getAllCustomersOrSearchByLastAndFirstName_not_found() {
        when(custRepo.findAll(
                any(Specification.class),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(of()));

        MyException response = assertThrows(MyException.class, () -> service.getAllCustomersOrSearchByLastAndFirstName(headers, "", 1, 1, "email"));

        assertThat(response.getErrorMessage()).isEqualTo(ErrorConstants.CUSTOMER_NOT_FOUND);
    }

    @Test
    void getOneCustomer_customer_found() {
        when(custRepo.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        MyResponse<Customer> oneCustomer = service.getOneCustomer(RANDOM_UUID1);
        assertThat(oneCustomer).isNotNull();
        assertThat(oneCustomer.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void getOneCustomer_customer_NOT_found() {

        myException = assertThrows(MyException.class, () -> service.getOneCustomer(RANDOM_UUID1));

        assertThat(myException.getErrorMessage()).isEqualTo(ErrorConstants.CUSTOMER_NOT_FOUND);
    }

    @Test
    void createOneCustomer_success() {

        CreateCustomerRequest request = CreateCustomerRequest.builder().build();
        copyProperties(customer, request);

        MyResponse<Customer> response = service.createOneCustomer(request);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void createOneCustomer_FAILED_CUSTOMER_EMAIL_ALREADY_EXISTS() {

        CreateCustomerRequest request = CreateCustomerRequest.builder().build();
        copyProperties(customer, request);

        when(custRepo.findByEmail(any())).thenReturn(Optional.of(customer));

        myException = assertThrows(MyException.class, () -> service.createOneCustomer(request));

        assertThat(myException.getErrorMessage()).isEqualTo(CUSTOMER_EMAIL_ALREADY_EXISTS);
    }

    @Test
    void updateOneCustomer_success() {
        PatchCustomerRequest request = PatchCustomerRequest.builder().build();
        copyProperties(customer, request);
        request.setAccountNumber(RANDOM_UUID1);
        when(custRepo.findById(any())).thenReturn(Optional.of(customer));
        MyResponse<Customer> response = service.updateOneCustomer(request);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void updateOneCustomer_fail_CUSTOMER_NOT_FOUND() {
        PatchCustomerRequest request = PatchCustomerRequest.builder().build();
        copyProperties(customer, request);

        when(custRepo.findById(any())).thenThrow(new MyException(ErrorConstants.CUSTOMER_NOT_FOUND));

        myException = assertThrows(MyException.class, () -> service.updateOneCustomer(request));

        assertThat(myException.getErrorMessage()).isEqualTo(ErrorConstants.CUSTOMER_NOT_FOUND);
    }

    @Test
    void deleteOneCustomer_success() {
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().accountNumber(RANDOM_UUID1).build();
        when(custRepo.findById(any())).thenReturn(Optional.of(customer));
        MyResponse<UUID> response = service.deleteOneCustomer(request);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void deleteOneCustomer_fail_CUSTOMER_NOT_FOUND() {
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().accountNumber(RANDOM_UUID1).build();

        when(custRepo.findById(any())).thenThrow(new MyException(ErrorConstants.CUSTOMER_NOT_FOUND));

        myException = assertThrows(MyException.class, () -> service.deleteOneCustomer(request));

        assertThat(myException.getErrorMessage()).isEqualTo(ErrorConstants.CUSTOMER_NOT_FOUND);
    }

    @Test
    void transferAmount() {
        TransferRequest request = initTransferAmount();

        //INITIALIZE PAYEE
        Customer payee = initCustomers();

        //INITIALIZE PAYER
        Customer payer = initCustomers();

        // SUCCESS
        when(custRepo.findById(request.getPayerAccountNumber())).thenReturn(Optional.of(payer));
        when(custRepo.findById(request.getPayeeAccountNumber())).thenReturn(Optional.of(payee));
        when(custRepo.saveAll(of(payee, payer))).thenReturn(of(payee, payer));
        MyResponse<TransferResponseDto> response = service.transferAmount(request);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);

        // PAYEE NOT FOUND
        when(custRepo.findById(request.getPayeeAccountNumber())).thenReturn(Optional.empty());
        myException = assertThrows(MyException.class, () -> service.transferAmount(request));
        assertThat(myException.getErrorMessage()).isEqualTo(PAYEE_NOT_FOUND);

        // PAYEE AND PAYER ACCOUNT NUMBERS ARE THE SAME
        request.setPayeeAccountNumber(RANDOM_UUID1);
        myException = assertThrows(MyException.class, () -> service.transferAmount(request));
        assertThat(myException.getErrorMessage()).isEqualTo(PAYER_AND_PAYEE_ACCOUNT_NUMBERS_ARE_THE_SAME);

        // INSUFFICIENT BALANCE
        request.setAmount(valueOf(1000.50));
        myException = assertThrows(MyException.class, () -> service.transferAmount(request));
        assertThat(myException.getErrorMessage()).isEqualTo(INSUFFICIENT_AMOUNT_FOR_PAYER);

        // PAYER NOT FOUND
        when(custRepo.findById(RANDOM_UUID1)).thenReturn(Optional.empty());
        myException = assertThrows(MyException.class, () -> service.transferAmount(request));
        assertThat(myException.getErrorMessage()).isEqualTo(PAYER_NOT_FOUND);
    }
}