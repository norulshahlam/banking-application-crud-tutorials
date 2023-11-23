package com.shah.bankingapplicationcrud.controller;

import com.shah.bankingapplicationcrud.impl.CustomerServiceImpl;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.*;
import com.shah.bankingapplicationcrud.model.response.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(
            value = "Retrieve all customers. Optional query param to search for customer containing by first or last name",
            response = BankingResponse.class,
            tags = "Retrieve all customers. Optional query param to search for customer containing by first or last name")
    @PostMapping(GET_ALL_CUSTOMERS)
    public ResponseEntity<BankingResponse<Page<Customer>>> searchCustomersByName(
            @RequestHeader HttpHeaders headers,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "email") String field,
            @RequestParam(defaultValue = "") String query) {
        return ResponseEntity.ok(service.getAllCustomersOrSearchByLastAndFirstName(headers, query, page, size, field));
    }

    @ApiOperation(
            value = "Retrieve one customer",
            response = BankingResponse.class,
            tags = "Retrieve one customer")
    @PostMapping(GET_ONE_CUSTOMER)
    public ResponseEntity<BankingResponse<Customer>> getOneCustomer(
            @ApiParam(defaultValue = "001d846e-4488-4ecc-84c2-9b6f1d130711")
            @Valid @RequestBody GetOneCustomerRequest request,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.getOneCustomer(request, headers));
    }

    @ApiOperation(value = "Add customer",
            response = BankingResponse.class,
            tags = "Add customer")
    @PostMapping(CREATE_CUSTOMER)
    public ResponseEntity<BankingResponse<Customer>> createOneCustomer(
            @Valid @RequestBody CreateCustomerRequest createCustomerRequest,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.createOneCustomer(createCustomerRequest, headers));
    }

    @ApiOperation(value = "Patch customer", response = BankingResponse.class, tags = "Add customer")
    @PostMapping(PATCH_CUSTOMER)
    public ResponseEntity<BankingResponse<Customer>> updateOneCustomer(
            @Valid @RequestBody PatchCustomerRequest createCustomerRequest,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.updateOneCustomer(createCustomerRequest, headers));
    }

    @ApiOperation(
            value = "Retrieve one customer",
            response = BankingResponse.class,
            tags = "Retrieve one customer")
    @PostMapping(DELETE_CUSTOMER)
    public ResponseEntity<BankingResponse<UUID>> deleteOneCustomer(
            @ApiParam(defaultValue = "001d846e-4488-4ecc-84c2-9b6f1d130711")
            @Valid @RequestBody GetOneCustomerRequest request,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.deleteOneCustomer(request, headers));
    }

    @ApiOperation(
            value = "Transfer amount",
            response = BankingResponse.class,
            tags = "Transfer amount")
    @PostMapping(TRANSFER)
    public ResponseEntity<BankingResponse<TransferResponseDto>> transferAmount(
            @Valid @RequestBody TransferRequest request,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.transferAmount(request, headers));
    }


}
