package com.shah.bankingapplicationcrud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shah.bankingapplicationcrud.impl.CustomerServiceImpl;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.CreateCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import com.shah.bankingapplicationcrud.model.response.CreateOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.SearchCustomerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest.builder;
import static com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse.success;
import static com.shah.bankingapplicationcrud.service.Initializer.initCustomers;
import static com.shah.bankingapplicationcrud.service.Initializer.initializeHeader;
import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.beans.BeanUtils.copyProperties;
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
    private String requestPayload = "";

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
        requestPayload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(CONTEXT_API_V1 + GET_ONE_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(SUCCESS))
                .andExpect(jsonPath("$.data.accBalance")
                        .value(response.getCustomer().getAccBalance().toString()));

    }

    @Test
    void searchCustomersByName() throws Exception {
        Page<Customer> pagedCustomers = new PageImpl<>(List.of(customer, customer, customer));
        SearchCustomerResponse response = SearchCustomerResponse.success(pagedCustomers);
        when(service.getAllCustomersOrSearchByLastAndFirstName(any(HttpHeaders.class), anyString(), anyInt(), anyInt(), anyString())).thenReturn(response);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("size", "20");
        params.add("field", "email");
        params.add("query", "Dorothy");

        mockMvc.perform(post(CONTEXT_API_V1 + GET_ALL_CUSTOMERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content")
                        .isArray())
                .andExpect(jsonPath("$.status").value(SUCCESS));
    }

    @Test
    void createOneCustomer() throws Exception {
        CreateOneCustomerResponse response = CreateOneCustomerResponse.success(customer);
        CreateCustomerRequest request = CreateCustomerRequest.builder().build();
        copyProperties(customer, request);
        requestPayload = objectMapper.writeValueAsString(request);

        when(service.createOneCustomer(any(), any(HttpHeaders.class))).thenReturn(response);
        mockMvc.perform(post(CONTEXT_API_V1 + CREATE_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email")
                        .isNotEmpty())
                .andExpect(jsonPath("$.status").value(SUCCESS));
    }

    @Test
    void createOneCustomer_failed_validation() throws Exception {
        CreateOneCustomerResponse response = CreateOneCustomerResponse.success(customer);
        CreateCustomerRequest request = CreateCustomerRequest.builder().build();
        customer.setGender("invalid gender");
        copyProperties(customer, request);
        requestPayload = objectMapper.writeValueAsString(request);

        when(service.createOneCustomer(any(), any(HttpHeaders.class))).thenReturn(response);
        mockMvc.perform(post(CONTEXT_API_V1 + CREATE_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.error")
                        .isNotEmpty())
                .andExpect(jsonPath("$.error.description")
                        .value("Gender must be Male or Female"));

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
    void codilityTest() {
        List<Integer> number = Arrays.asList(1, 3, 6, 4, 1, 2);
        int min = 0;
        int max = number.stream().max(comparing(Integer::valueOf)).get();
        System.out.println(max);
    }
}