package com.shah.bankingapplicationcrud.model.response;

import com.shah.bankingapplicationcrud.exception.CrudError;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.shah.bankingapplicationcrud.model.constant.CommonConstants.FAIL;
import static com.shah.bankingapplicationcrud.model.constant.CommonConstants.SUCCESS;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOneCustomerResponse {

    @Schema(description = "Possible value: \n" +
            " + SUCCESS\n" +
            " + FAILED\n")
    private String status;

    @Schema(description = "Details of customer")
    private Customer customer;

    private CrudError error;

    public static CreateOneCustomerResponse success(Customer customer) {
        return new CreateOneCustomerResponse(SUCCESS, customer, null);
    }

    public static CreateOneCustomerResponse fail(Customer customer, CrudError error) {
        return new CreateOneCustomerResponse(FAIL, customer, error);
    }

}
