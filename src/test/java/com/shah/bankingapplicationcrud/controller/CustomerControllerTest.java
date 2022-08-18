package com.shah.bankingapplicationcrud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shah.bankingapplicationcrud.impl.CustomerServiceImpl;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.GET_ALL_CUSTOMERS;
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

    @InjectMocks
    CustomerServiceImpl service;
    private Customer customer;

    @BeforeEach
    void setUp() {

        customer.builder().email(randomString() + "@email.com").firstName(randomString())
                .lastName(randomString()).gender("male").age(randomInt()).country("Singapore")
                .birthDate(new Date(1987 - 03 - 29)).accountNumber(UUID.randomUUID()).build();
    }

    @Test
    void searchCustomersByName() {

    }

    @Test
    void getOneCustomer() throws Exception {
        GetOneCustomerResponse response = GetOneCustomerResponse.success(customer);
        when(service.getOneCustomer(any(), any())).thenReturn(response);
        String requestPayload = objectMapper.writeValueAsString(customer);

        mockMvc.perform(post(GET_ALL_CUSTOMERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id")
                        .value(response.getCustomer().getAccBalance().toString()));

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

    private String randomString() {
        return RandomStringUtils.randomAlphabetic(6);
    }

    private int randomInt() {
        return ThreadLocalRandom.current().nextInt(21, 55 + 1);
    }
}