package com.shah.bankingapplicationcrud.controller;

import com.shah.bankingapplicationcrud.impl.CustomerServiceImpl;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.*;
import com.shah.bankingapplicationcrud.model.response.BankingResponse;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static com.shah.bankingapplicationcrud.validation.ValidateHeaders.validateHeaders;


@RequestMapping(CONTEXT_API_V1)
@Data
@RestController
@Validated
@ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 302, message = "Found"),
        @ApiResponse(code = 404, message = "Not found"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 500, message = "Service Unavaliable")})
public class CustomerController {


    @Autowired
    private final CustomerServiceImpl service;

    @Operation(description = "Retrieve all customers. Optional query param to search for customer containing by first or last name",
            tags = "Retrieve all customers")
    @GetMapping(GET_ALL_CUSTOMERS)
    public ResponseEntity<BankingResponse<Page<Customer>>> searchCustomersByName(
            @RequestHeader HttpHeaders headers,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "email") String field,
            @RequestParam(defaultValue = "") String query) {
        validateHeaders(headers);
        return ResponseEntity.ok(service.getAllCustomersOrSearchByLastAndFirstName(headers, query, page, size, field));
    }

    @Operation(description = "Get one customer",
            tags = "Get one customer")
    @GetMapping(GET_ONE_CUSTOMER + "/{request}")
    public ResponseEntity<BankingResponse<Customer>> getOneCustomer(
            @ApiParam(defaultValue = "001d846e-4488-4ecc-84c2-9b6f1d130711")
            @Valid @PathVariable UUID request,
            @RequestHeader HttpHeaders headers) {
        validateHeaders(headers);
        return ResponseEntity.ok(service.getOneCustomer(request));
    }

    @Operation(description = "Add customer",
            tags = "Add customer")
    @PostMapping(CREATE_CUSTOMER)
    public ResponseEntity<BankingResponse<Customer>> createOneCustomer(
            @Valid @RequestBody CreateCustomerRequest createCustomerRequest,
            @RequestHeader HttpHeaders headers) {
        validateHeaders(headers);
        return ResponseEntity.ok(service.createOneCustomer(createCustomerRequest));
    }

    @Operation(description = "Patch customer",
            tags = "Patch customer")
    @PatchMapping(PATCH_CUSTOMER)
    public ResponseEntity<BankingResponse<Customer>> updateOneCustomer(
            @Valid @RequestBody PatchCustomerRequest createCustomerRequest,
            @RequestHeader HttpHeaders headers) {
        validateHeaders(headers);
        return ResponseEntity.ok(service.updateOneCustomer(createCustomerRequest));
    }

    @Operation(description = "Delete customer",
            tags = "Delete customer")
    @DeleteMapping(DELETE_CUSTOMER)
    public ResponseEntity<BankingResponse<UUID>> deleteOneCustomer(
            @ApiParam(defaultValue = "001d846e-4488-4ecc-84c2-9b6f1d130711")
            @Valid @RequestBody GetOneCustomerRequest request,
            @RequestHeader HttpHeaders headers) {
        validateHeaders(headers);
        return ResponseEntity.ok(service.deleteOneCustomer(request));
    }

    @Operation(description = "Transfer amount",
            tags = "Transfer amount")
    @PostMapping(TRANSFER)
    public ResponseEntity<BankingResponse<TransferResponseDto>> transferAmount(
            @Valid @RequestBody TransferRequest request,
            @RequestHeader HttpHeaders headers) {
        validateHeaders(headers);
        return ResponseEntity.ok(service.transferAmount(request));
    }
}
