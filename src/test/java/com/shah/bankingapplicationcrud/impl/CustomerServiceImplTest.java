package com.shah.bankingapplicationcrud.impl;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse;
import com.shah.bankingapplicationcrud.repository.CustomerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static java.util.Optional.of;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.MockitoAnnotations.openMocks;
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

        setField(service, "custRepo", custRepo);

        openMocks(this);

        headers.add(X_SOURCE_COUNTRY, SG);
        headers.add(X_CORRELATION_ID, RANDOM_UUID);
        headers.add(X_SOURCE_DATE_TIME, LocalDate.now().toString());

        customer = Customer.builder()
                .email(randomString() + "@email.com")
                .firstName(randomString())
                .lastName(randomString())
                .gender("male")
                .age(randomInt())
                .country("Singapore")
                .birthDate(new Date(1987 - 03 - 29))
                .accountNumber(fromString(RANDOM_UUID))
                .build();
    }

    @Test
    void getAllCustomersOrSearchByLastAndFirstName() {
    }

    @Test
    void getOneCustomer() {

        lenient().when(custRepo.findById(any(UUID.class))).thenReturn(of(customer));
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().id(RANDOM_UUID).build();

        GetOneCustomerResponse oneCustomer = service.getOneCustomer(request, headers);
        System.out.println(oneCustomer);
        assertThat(oneCustomer).isNotNull();
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