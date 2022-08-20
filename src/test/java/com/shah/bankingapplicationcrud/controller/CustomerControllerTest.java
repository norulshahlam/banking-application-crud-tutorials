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

import java.util.concurrent.ThreadLocalRandom;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest.builder;
import static com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse.success;
import static com.shah.bankingapplicationcrud.service.Initializer.initCustomers;
import static com.shah.bankingapplicationcrud.service.Initializer.initializeHeader;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
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
    private HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void setUp() {
        openMocks(this);
        headers = initializeHeader();
        customer = initCustomers();
    }

    @Test
    void getOneCustomer() throws Exception {
        GetOneCustomerResponse response = success(customer);
        GetOneCustomerRequest request = builder().id(RANDOM_UUID1).build();
        when(service.getOneCustomer(any(GetOneCustomerRequest.class), any(HttpHeaders.class))).thenReturn(response);
        String requestPayload = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(CONTEXT_API_V1 + GET_ONE_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.accBalance")
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