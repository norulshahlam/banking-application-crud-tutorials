package com.shah.bankingapplicationcrud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shah.bankingapplicationcrud.exception.CrudError;
import com.shah.bankingapplicationcrud.exception.CrudException;
import com.shah.bankingapplicationcrud.impl.CustomerServiceImpl;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.*;
import com.shah.bankingapplicationcrud.model.response.BankingResponse;
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
import static com.shah.bankingapplicationcrud.model.enums.ResponseStatus.FAILURE;
import static com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest.builder;
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
        BankingResponse<Customer> response = BankingResponse.successResponse(customer);
        GetOneCustomerRequest request = builder().accountNumber(RANDOM_UUID1).build();
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
                        .value(response.getData().getAccBalance().toString()));

    }

    @Test
    void getOneCustomer_customer_not_found() throws Exception {
        CrudException e = new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND);
        BankingResponse<Customer> response = BankingResponse.failureResponse(constructErrorForCrudException(e));

        GetOneCustomerRequest request = builder().accountNumber(RANDOM_UUID1).build();
        when(service.getOneCustomer(any(GetOneCustomerRequest.class), any(HttpHeaders.class))).thenReturn(response);
        requestPayload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(CONTEXT_API_V1 + GET_ONE_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(FAILURE.name()))
                .andExpect(jsonPath("$.error")
                        .isNotEmpty());

    }

    @Test
    void searchCustomersByName_success() throws Exception {
        Page<Customer> pagedCustomers = new PageImpl<>(List.of(customer, customer, customer));
        BankingResponse<Page<Customer>> response = BankingResponse.successResponse(pagedCustomers);
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
        BankingResponse<Page<Customer>> response = BankingResponse.failureResponse((constructErrorForCrudException(e)));
        when(service.getAllCustomersOrSearchByLastAndFirstName(any(HttpHeaders.class), anyString(), anyInt(), anyInt(), anyString())).thenReturn(response);

        mockMvc.perform(post(CONTEXT_API_V1 + GET_ALL_CUSTOMERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .params(searchCustomerParams))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error.errorCode")
                        .isNotEmpty())
                .andExpect(jsonPath("$.status").value(FAILURE.name()));
    }

    @Test
    void createOneCustomer_success() throws Exception {
        BankingResponse<Customer> response = BankingResponse.successResponse(customer);
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
        BankingResponse<Customer> response = BankingResponse.successResponse(customer);
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
        BankingResponse<Customer> response = BankingResponse.successResponse(customer);
        PatchCustomerRequest request = PatchCustomerRequest.builder().build();
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
        BankingResponse<Customer> response = BankingResponse.failureResponse(constructErrorForCrudException(e));
        PatchCustomerRequest request = PatchCustomerRequest.builder().build();
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
                .andExpect(jsonPath("$.status").value(FAILURE.name()));
    }

    @Test
    void deleteOneCustomer_success() throws Exception {
        UUID id = RANDOM_UUID1;
        GetOneCustomerRequest request = builder().accountNumber(RANDOM_UUID1).build();
        BankingResponse<UUID> response = BankingResponse.successResponse(id);
        requestPayload = objectMapper.writeValueAsString(request);
        when(service.deleteOneCustomer(any(), any(HttpHeaders.class))).thenReturn(response);

        mockMvc.perform(post(CONTEXT_API_V1 + DELETE_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data")
                        .isNotEmpty())
                .andExpect(jsonPath("$.status").value(SUCCESS));
    }

    @Test
    void deleteOneCustomer_customer_not_found() throws Exception {
        UUID id = RANDOM_UUID1;
        CrudException e = new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND);
        GetOneCustomerRequest request = builder().accountNumber(RANDOM_UUID1).build();
        BankingResponse<UUID> response = BankingResponse.failureResponse(constructErrorForCrudException(e));
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
                .andExpect(jsonPath("$.status").value(FAILURE.name()));
    }

    @Test
    void transferAmount_success() throws Exception {
        TransferRequest request = initTransferAmount();
        TransferResponseDto data = initTransferResponseDto();
        BankingResponse<TransferResponseDto> response = BankingResponse.successResponse(data);
        requestPayload = objectMapper.writeValueAsString(request);
        when(service.transferAmount(any(TransferRequest.class), any(HttpHeaders.class))).thenReturn(response);

        mockMvc.perform(post(CONTEXT_API_V1 + TRANSFER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data")
                        .isNotEmpty())
                .andExpect(jsonPath("$.status").value(SUCCESS));
    }

    @Test
    void transferAmount_insufficient_amount() throws Exception {
        CrudException e = new CrudException(AC_BAD_REQUEST, INSUFFICIENT_AMOUNT);
        TransferRequest request = initTransferAmount();
        TransferResponseDto data = initTransferResponseDto();
        BankingResponse<TransferResponseDto> response = BankingResponse.failureResponse(CrudError.constructErrorForCrudException(e));
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
                .andExpect(jsonPath("$.status").value(FAILURE.name()));
    }

}