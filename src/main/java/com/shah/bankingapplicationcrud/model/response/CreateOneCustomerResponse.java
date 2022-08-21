package com.shah.bankingapplicationcrud.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shah.bankingapplicationcrud.exception.CrudError;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.FAIL;
import static com.shah.bankingapplicationcrud.constant.CommonConstants.SUCCESS;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CreateOneCustomerResponse {

    @Schema(description = "Possible value: \n" +
            " + SUCCESS\n" +
            " + FAILED\n")
    private String status;

    @Schema(description = "Details of customer")
    @JsonProperty("data")
    private Customer customer;

    private CrudError error;

    public static CreateOneCustomerResponse success(Customer customer) {
        log.info("Create / update one customer SUCCESS...");
        return new CreateOneCustomerResponse(SUCCESS, customer, null);
    }

    public static CreateOneCustomerResponse fail(Customer customer, CrudError error) {
        log.error("Create / update one customer FAIL...");
        return new CreateOneCustomerResponse(FAIL, customer, error);
    }

}
