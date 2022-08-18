package com.shah.bankingapplicationcrud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shah.bankingapplicationcrud.impl.CustomerServiceImpl;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private CustomerServiceImpl service;
    private Customer customer;
    private HttpHeaders headers = new HttpHeaders();;

    @BeforeEach
    void setUp() {

//        headers.add(X_SOURCE_COUNTRY,SG);
//        headers.add(X_CORRELATION_ID,RANDOM_UUID);
//        headers.add(X_SOURCE_DATE_TIME, LocalDate.now().toString());

        customer.builder().email(randomString() + "@email.com").firstName(randomString())
                .lastName(randomString()).gender("male").age(randomInt()).country("Singapore")
                .birthDate(new Date(1987 - 03 - 29)).accountNumber(UUID.randomUUID()).build();
    }

    @Test
    void getOneCustomer() throws Exception {
        GetOneCustomerResponse response = GetOneCustomerResponse.success(customer);
        GetOneCustomerRequest request = GetOneCustomerRequest.builder().id(RANDOM_UUID).build();
        when(service.getOneCustomer(any(GetOneCustomerRequest.class), any(HttpHeaders.class))).thenReturn(response);
        String requestPayload = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(GET_ONE_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers)
                        )
                .andDo(print())

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id")
                        .value(response.getCustomer().getAccBalance().toString()));

    }

    @Test
    void searchCustomersByName() {

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

    public static String randomString() {
        return RandomStringUtils.randomAlphabetic(6);
    }

    public static int randomInt() {
        return ThreadLocalRandom.current().nextInt(21, 55 + 1);
    }
}