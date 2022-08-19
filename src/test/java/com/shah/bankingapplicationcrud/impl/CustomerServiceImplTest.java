package com.shah.bankingapplicationcrud.impl;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.SearchCustomerResponse;
import com.shah.bankingapplicationcrud.repository.CustomerRepository;
import org.apache.commons.lang3.RandomStringUtils;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static com.shah.bankingapplicationcrud.exception.CrudErrorCodes.CUSTOMER_NOT_FOUND;
import static java.util.Optional.of;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
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

    void initHeaders(){
        headers.add(X_SOURCE_COUNTRY, SG);
        headers.add(X_CORRELATION_ID, RANDOM_UUID1);
        headers.add(X_SOURCE_DATE_TIME, LocalDate.now().toString());
    }

    void initCustomers(){
        customer = Customer.builder()
                .email(randomString() + "@email.com")
                .firstName(randomString())
                .lastName(randomString())
                .gender("male")
                .age(randomInt())
                .country("Singapore")
                .birthDate(new Date(1987 - 03 - 29))
                .accountNumber(fromString(RANDOM_UUID1))
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
                .thenReturn(Page.empty());

        SearchCustomerResponse response = service.getAllCustomersOrSearchByLastAndFirstName(headers, "bob", 1, 1, "email");

        assertThat(response.getCustomers().get().findFirst()).isEmpty();
    }

    @Test
    void getOneCustomer_customer_found() {
        when(custRepo.findById(any(UUID.class))).thenReturn(of(customer));
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().id(RANDOM_UUID1).build();
        GetOneCustomerResponse oneCustomer = service.getOneCustomer(request, headers);
        assertThat(oneCustomer).isNotNull();
        assertThat(oneCustomer.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void getOneCustomer_customer_not_found() {
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().id(RANDOM_UUID1).build();
        GetOneCustomerResponse oneCustomer = service.getOneCustomer(request, headers);
        assertThat(oneCustomer.getStatus()).isEqualTo(FAIL);
        assertThat(oneCustomer.getError().getErrorCode()).isEqualTo(CUSTOMER_NOT_FOUND.getCode());
    }

    @Test
    void createOneCustomer() {
    }

    @Test
    void updateOneCustomer() {
    }

    @Test
    void deleteOneCustomer() {
    }

    @Test
    void transferAmount() {
    }

    @Test
    void getNullPropertyNames() {
    }

    public static String randomString() {
        return RandomStringUtils.randomAlphabetic(6);
    }

    public static int randomInt() {
        return ThreadLocalRandom.current().nextInt(21, 55 + 1);
    }

}