package com.shah.bankingapplicationcrud.impl;

import com.shah.bankingapplicationcrud.exception.CrudException;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.CreateCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.PatchCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.TransferRequest;
import com.shah.bankingapplicationcrud.model.response.*;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static com.shah.bankingapplicationcrud.exception.CrudErrorCodes.*;
import static java.math.BigDecimal.valueOf;
import static java.util.List.of;
import static java.util.UUID.fromString;
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
    private final HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void setUp() {
        openMocks(this);
        setField(service, "custRepo", custRepo);

        initHeaders();
        initCustomers();
    }

    void initHeaders() {
        headers.add(X_SOURCE_COUNTRY, SG);
        headers.add(X_CORRELATION_ID, RANDOM_UUID1);
        headers.add(X_SOURCE_DATE_TIME, LocalDate.now().toString());
    }

    void initCustomers() {
        customer = Customer.builder()
                .email("norulshahlam@gmail.com")
                .firstName("norulshahlam")
                .lastName("bin mohsen")
                .gender("male")
                .age(21)
                .country("Singapore")
                .birthDate(new Date(2000 - 3 - 29))
                .accountNumber(fromString(RANDOM_UUID1))
                .accBalance(valueOf(10.50))
                .updatedAt(ZonedDateTime.now())
                .build();
    }

    @Test
    void getAllCustomersOrSearchByLastAndFirstName_found() {
        Page<Customer> pagedCustomers = new PageImpl<>(List.of(customer));
        when(custRepo.findAll(
                any(Specification.class),
                any(Pageable.class)))
                .thenReturn(pagedCustomers);

        SearchCustomerResponse response = service.getAllCustomersOrSearchByLastAndFirstName(headers, "bob", 1, 1, "email");

        assertThat(response.getCustomers().get().findFirst().get()).isEqualTo(customer);
    }

    @Test
    void getAllCustomersOrSearchByLastAndFirstName_not_found() {
        when(custRepo.findAll(
                any(Specification.class),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(of()));

        SearchCustomerResponse response = service.getAllCustomersOrSearchByLastAndFirstName(headers, "", 1, 1, "email");

        assertThat(response.getStatus()).isEqualTo(FAIL);
        assertThat(response.getError().getErrorCode()).isEqualTo(CUSTOMER_NOT_FOUND.getCode());
    }

    @Test
    void getOneCustomer_customer_found() {
        when(custRepo.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().id(RANDOM_UUID1).build();
        GetOneCustomerResponse oneCustomer = service.getOneCustomer(request, headers);
        assertThat(oneCustomer).isNotNull();
        assertThat(oneCustomer.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void getOneCustomer_customer_not_found() {
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().id(RANDOM_UUID1).build();
        GetOneCustomerResponse response = service.getOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAIL);
        assertThat(response.getError().getErrorCode()).isEqualTo(CUSTOMER_NOT_FOUND.getCode());
    }

    @Test
    void createOneCustomer_success() {

        CreateCustomerRequest request = CreateCustomerRequest.builder().build();
        copyProperties(customer, request);

        CreateOneCustomerResponse response = service.createOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);

        //createOneCustomer_FAILED
        when(custRepo.save(any())).thenThrow(new CrudException(AC_BAD_REQUEST, AC_INTERNAL_SERVER_ERROR));
        response = service.createOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAIL);
        assertThat(response.getError().getErrorCode()).isEqualTo(AC_INTERNAL_SERVER_ERROR.getCode());
    }

    @Test
    void updateOneCustomer_success() {
        PatchCustomerRequest request = PatchCustomerRequest.builder().build();
        copyProperties(customer, request);
        request.setId(fromString(RANDOM_UUID1));
        when(custRepo.findById(any())).thenReturn(Optional.of(customer));
        CreateOneCustomerResponse response = service.updateOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void updateOneCustomer_fail() {
        PatchCustomerRequest request = PatchCustomerRequest.builder().build();
        copyProperties(customer, request);
        CreateOneCustomerResponse response = service.updateOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAIL);
        assertThat(response.getError().getErrorCode()).isEqualTo(EMPTY_ID.getCode());
    }

    @Test
    void deleteOneCustomer_success() {
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().id(RANDOM_UUID1).build();
        when(custRepo.findById(any())).thenReturn(Optional.of(customer));
        DeleteOneCustomerResponse response = service.deleteOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void deleteOneCustomer_fail() {
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().id(RANDOM_UUID1).build();
        DeleteOneCustomerResponse response = service.deleteOneCustomer(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAIL);
        assertThat(response.getError().getErrorCode()).isEqualTo(CUSTOMER_NOT_FOUND.getCode());
    }

    @Test
    void transferAmount() {
        TransferRequest request = TransferRequest.builder()
                .payerAccountNumber(RANDOM_UUID1)
                .payeeAccountNumber(RANDOM_UUID2)
                .amount(valueOf(1.50))
                .build();

        //INITIALIZE PAYEE
        Customer payee = customer;

        //INITIALIZE PAYER
        Customer payer = customer;

        when(custRepo.findById(fromString(RANDOM_UUID1))).thenReturn(Optional.of(payer));
        when(custRepo.findById(fromString(RANDOM_UUID2))).thenReturn(Optional.of(payee));
        when(custRepo.saveAll(of(payee, payer))).thenReturn(of(payee, payer));

        TransferAmountResponse response = service.transferAmount(request, headers);
        assertThat(response.getStatus()).isEqualTo(SUCCESS);

        // PAYEE NOT FOUND
        when(custRepo.findById(fromString(RANDOM_UUID2))).thenReturn(Optional.empty());
        response = service.transferAmount(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAIL);
        assertThat(response.getError().getErrorCode()).isEqualTo(PAYEE_ACCOUNT_NOT_FOUND.getCode());

        // INSUFFICIENT BALANCE
        request.setAmount(valueOf(1000.50));
        response = service.transferAmount(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAIL);
        assertThat(response.getError().getErrorCode()).isEqualTo(INSUFFICIENT_AMOUNT.getCode());

        // PAYER NOT FOUND
        when(custRepo.findById(fromString(RANDOM_UUID1))).thenReturn(Optional.empty());
        response = service.transferAmount(request, headers);
        assertThat(response.getStatus()).isEqualTo(FAIL);
        assertThat(response.getError().getErrorCode()).isEqualTo(PAYER_ACCOUNT_NOT_FOUND.getCode());
    }
}