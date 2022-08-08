package com.shah.bankingapplicationcrud.controller;

import com.shah.bankingapplicationcrud.impl.CustomerServiceImpl;
import com.shah.bankingapplicationcrud.model.request.CreateCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.PatchCustomerRequest;
import com.shah.bankingapplicationcrud.model.response.*;
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

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;


@RequestMapping("/api/v1")
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
    @PostMapping(value = GET_ALL_CUSTOMERS + "/{page}/{size}/{field}")
    public ResponseEntity<GetAllCustomerResponse> getAllCustomers(
            @RequestHeader HttpHeaders headers,
            @PathVariable int page,
            @PathVariable int size,
            @PathVariable(required = false) String field) {
        return ResponseEntity.ok(service.getAllCustomers(headers, page, size, field));
    }

    @ApiOperation(
            value = "Search customer containing by first or last name",
            response = GetOneCustomerResponse.class,
            tags = "Search customer containing by first or last name")
    @PostMapping(GET_CUSTOMERS_BY_FIRSTNAME_OR_LASTNAME_LIKE + "/{name}/{page}/{size}/{field}")
    public ResponseEntity<SearchCustomerResponse> searchCustomersByName(
            @RequestHeader HttpHeaders headers,
            @PathVariable int page,
            @PathVariable int size,
            @PathVariable(required = false) String field,
            @PathVariable String name) {
        return ResponseEntity.ok(service.searchCustomersByName(headers, name,page,size,field));
    }

    @ApiOperation(
            value = "Retrieve one customer",
            response = GetOneCustomerResponse.class,
            tags = "Retrieve one customer")
    @PostMapping(GET_ONE_CUSTOMER)
    public ResponseEntity<GetOneCustomerResponse> getOneCustomer(
            @ApiParam(defaultValue = "001d846e-4488-4ecc-84c2-9b6f1d130711")
            @Valid @RequestBody GetOneCustomerRequest request,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.getOneCustomer(request, headers));
    }

    @ApiOperation(value = "Add customer", response = CreateOneCustomerResponse.class, tags = "Add customer")
    @PostMapping(CREATE_CUSTOMER)
    public ResponseEntity<CreateOneCustomerResponse> createOneCustomer(
            @Valid @RequestBody CreateCustomerRequest createCustomerRequest,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.createOneCustomer(createCustomerRequest, headers));
    }

    @ApiOperation(value = "Patch customer", response = CreateOneCustomerResponse.class, tags = "Add customer")
    @PostMapping(PATCH_CUSTOMER)
    public ResponseEntity<CreateOneCustomerResponse> patchOneCustomer(
            @Valid @RequestBody PatchCustomerRequest createCustomerRequest,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.patchOneCustomer(createCustomerRequest, headers));
    }

    @ApiOperation(
            value = "Retrieve one customer",
            response = DeleteOneCustomerResponse.class,
            tags = "Retrieve one customer")
    @PostMapping(DELETE_CUSTOMER)
    public ResponseEntity<DeleteOneCustomerResponse> deleteOneCustomer(
            @ApiParam(defaultValue = "001d846e-4488-4ecc-84c2-9b6f1d130711")
            @Valid @RequestBody GetOneCustomerRequest request,
            @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(service.deleteOneCustomer(request, headers));
    }


}
