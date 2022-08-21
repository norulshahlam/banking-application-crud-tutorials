package com.shah.bankingapplicationcrud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shah.bankingapplicationcrud.exception.CrudError;
import com.shah.bankingapplicationcrud.exception.CrudException;
import com.shah.bankingapplicationcrud.impl.CustomerServiceImpl;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.CreateCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.PatchCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.TransferRequest;
import com.shah.bankingapplicationcrud.model.response.*;
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

import java.util.List;
import java.util.UUID;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static com.shah.bankingapplicationcrud.exception.CrudError.constructErrorForCrudException;
import static com.shah.bankingapplicationcrud.exception.CrudErrorCodes.*;
import static com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest.builder;
import static com.shah.bankingapplicationcrud.model.response.DeleteOneCustomerResponse.fail;
import static com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse.success;
import static com.shah.bankingapplicationcrud.model.response.SearchCustomerResponse.success;
import static com.shah.bankingapplicationcrud.model.response.TransferAmountResponse.fail;
import static com.shah.bankingapplicationcrud.service.Initializer.*;
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
    MultiValueMap<String, String> searchCustomerParams = new LinkedMultiValueMap<>();

    @BeforeEach
    void setUp() {
        openMocks(this);
        headers = initializeHeader();
        customer = initCustomers();
        searchCustomerParams = initSearchCustomerParams();
    }

    @Test
    void getOneCustomer_success() throws Exception {
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
    void getOneCustomer_customer_not_found() throws Exception {
        CrudException e = new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND);
        GetOneCustomerResponse response = GetOneCustomerResponse.fail(null, constructErrorForCrudException(e));

        GetOneCustomerRequest request = builder().id(RANDOM_UUID1).build();
        when(service.getOneCustomer(any(GetOneCustomerRequest.class), any(HttpHeaders.class))).thenReturn(response);
        requestPayload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(CONTEXT_API_V1 + GET_ONE_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(FAIL))
                .andExpect(jsonPath("$.error")
                        .isNotEmpty());

    }

    @Test
    void searchCustomersByName_success() throws Exception {
        Page<Customer> pagedCustomers = new PageImpl<>(List.of(customer, customer, customer));
        SearchCustomerResponse response = success(pagedCustomers);
        when(service.getAllCustomersOrSearchByLastAndFirstName(any(HttpHeaders.class), anyString(), anyInt(), anyInt(), anyString())).thenReturn(response);

        mockMvc.perform(post(CONTEXT_API_V1 + GET_ALL_CUSTOMERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .params(searchCustomerParams))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content")
                        .isArray())
                .andExpect(jsonPath("$.status").value(SUCCESS));
    }

    @Test
    void searchCustomersByName_customer_not_found() throws Exception {
        CrudException e = new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND);
        SearchCustomerResponse response = SearchCustomerResponse.fail(constructErrorForCrudException(e));
        when(service.getAllCustomersOrSearchByLastAndFirstName(any(HttpHeaders.class), anyString(), anyInt(), anyInt(), anyString())).thenReturn(response);

        mockMvc.perform(post(CONTEXT_API_V1 + GET_ALL_CUSTOMERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .params(searchCustomerParams))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error.errorCode")
                        .isNotEmpty())
                .andExpect(jsonPath("$.status").value(FAIL));
    }

    @Test
    void createOneCustomer_success() throws Exception {
        CreateOneCustomerResponse response = CreateOneCustomerResponse.success(customer);
        CreateCustomerRequest request = CreateCustomerRequest.builder().build();
        copyProperties(customer, request);
        requestPayload = objectMapper.writeValueAsString(request);

        when(service.createOneCustomer(any(CreateCustomerRequest.class), any(HttpHeaders.class))).thenReturn(response);
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
    void updateOneCustomer_success() throws Exception {
        CreateOneCustomerResponse response = CreateOneCustomerResponse.success(customer);
        CreateCustomerRequest request = CreateCustomerRequest.builder().build();
        copyProperties(customer, request);
        when(service.updateOneCustomer(any(PatchCustomerRequest.class), any(HttpHeaders.class))).thenReturn(response);
        requestPayload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(CONTEXT_API_V1 + PATCH_CUSTOMER)
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
    void updateOneCustomer_customer_not_found() throws Exception {
        CrudException e = new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND);
        CreateOneCustomerResponse response = CreateOneCustomerResponse.fail(customer, constructErrorForCrudException(e));
        CreateCustomerRequest request = CreateCustomerRequest.builder().build();
        customer.setAccountNumber(UUID.randomUUID());
        copyProperties(customer, request);
        when(service.updateOneCustomer(any(PatchCustomerRequest.class), any(HttpHeaders.class))).thenReturn(response);
        requestPayload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(CONTEXT_API_V1 + PATCH_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error")
                        .isNotEmpty())
                .andExpect(jsonPath("$.status").value(FAIL));
    }

    @Test
    void deleteOneCustomer_success() throws Exception {
        UUID id = UUID.fromString(RANDOM_UUID1);
        GetOneCustomerRequest request = builder().id(RANDOM_UUID1).build();
        DeleteOneCustomerResponse response = DeleteOneCustomerResponse.success(id);
        requestPayload = objectMapper.writeValueAsString(request);
        when(service.deleteOneCustomer(any(), any(HttpHeaders.class))).thenReturn(response);

        mockMvc.perform(post(CONTEXT_API_V1 + DELETE_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error")
                        .isEmpty())
                .andExpect(jsonPath("$.status").value(SUCCESS));
    }

    @Test
    void deleteOneCustomer_customer_not_found() throws Exception {
        UUID id = UUID.fromString(RANDOM_UUID1);
        CrudException e = new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND);
        GetOneCustomerRequest request = builder().id(RANDOM_UUID1).build();
        DeleteOneCustomerResponse response = fail(id, constructErrorForCrudException(e));
        requestPayload = objectMapper.writeValueAsString(request);
        when(service.deleteOneCustomer(any(), any(HttpHeaders.class))).thenReturn(response);

        mockMvc.perform(post(CONTEXT_API_V1 + DELETE_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error.errorCode")
                        .value("CRUD-20001"))
                .andExpect(jsonPath("$.status").value(FAIL));
    }

    @Test
    void transferAmount_success() throws Exception {
        TransferRequest request = initTransferAmount();
        TransferResponseDto data = initTransferResponseDto();
        TransferAmountResponse response = TransferAmountResponse.success(data);
        requestPayload = objectMapper.writeValueAsString(request);
        when(service.transferAmount(any(TransferRequest.class), any(HttpHeaders.class))).thenReturn(response);

        mockMvc.perform(post(CONTEXT_API_V1 + TRANSFER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error")
                        .isEmpty())
                .andExpect(jsonPath("$.status").value(SUCCESS));
    }

    @Test
    void transferAmount_insufficient_amount() throws Exception {
        CrudException e = new CrudException(AC_BAD_REQUEST, INSUFFICIENT_AMOUNT);
        TransferRequest request = initTransferAmount();
        TransferResponseDto data = initTransferResponseDto();
        TransferAmountResponse response = fail(data, CrudError.constructErrorForCrudException(e));
        requestPayload = objectMapper.writeValueAsString(request);
        when(service.transferAmount(any(TransferRequest.class), any(HttpHeaders.class))).thenReturn(response);

        mockMvc.perform(post(CONTEXT_API_V1 + TRANSFER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error")
                        .isNotEmpty())
                .andExpect(jsonPath("$.status").value(FAIL));
    }

}