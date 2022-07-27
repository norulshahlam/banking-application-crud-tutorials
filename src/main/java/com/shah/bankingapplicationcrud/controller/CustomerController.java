package com.shah.bankingapplicationcrud.controller;

import com.shah.bankingapplicationcrud.impl.CustomerServiceImpl;
import com.shah.bankingapplicationcrud.model.request.CreateCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.PatchCustomerRequest;
import com.shah.bankingapplicationcrud.model.response.CreateOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.DeleteOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.GetAllCustomerResponse;
import com.shah.bankingapplicationcrud.model.response.GetOneCustomerResponse;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping("/crud-api")
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
            value = "Retrieve all customer",
            response = GetOneCustomerResponse.class,
            tags = "Retrieve all customer")
    @PostMapping("/get-all-customers")
    public ResponseEntity<GetAllCustomerResponse> getAllCustomers(
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.getAllCustomers(headers));
    }


    @ApiOperation(
            value = "Retrieve one customer",
            response = GetOneCustomerResponse.class,
            tags = "Retrieve one customer")
    @PostMapping("/get-one-customer")
    public ResponseEntity<GetOneCustomerResponse> getOneCustomer(
            @ApiParam(defaultValue = "001d846e-4488-4ecc-84c2-9b6f1d130711")
            @Valid @RequestBody GetOneCustomerRequest request,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.getOneCustomer(request, headers));
    }

    @ApiOperation(value = "Add customer", response = CreateOneCustomerResponse.class, tags = "Add customer")
    @PostMapping("/create-customer")
    public ResponseEntity<CreateOneCustomerResponse> createOneCustomer(
            @Valid @RequestBody CreateCustomerRequest createCustomerRequest,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.createOneCustomer(createCustomerRequest, headers));
    }

    @ApiOperation(value = "Patch customer", response = CreateOneCustomerResponse.class, tags = "Add customer")
    @PostMapping("/patch-customer")
    public ResponseEntity<CreateOneCustomerResponse> patchOneCustomer(
            @Valid @RequestBody PatchCustomerRequest createCustomerRequest,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.patchOneCustomer(createCustomerRequest, headers));
    }

    @ApiOperation(
            value = "Retrieve one customer",
            response = DeleteOneCustomerResponse.class,
            tags = "Retrieve one customer")
    @PostMapping("/delete-customer")
    public ResponseEntity<DeleteOneCustomerResponse> deleteOneCustomer(
            @ApiParam(defaultValue = "001d846e-4488-4ecc-84c2-9b6f1d130711")
            @Valid @RequestBody GetOneCustomerRequest request,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.deleteOneCustomer(request, headers));
    }
}
