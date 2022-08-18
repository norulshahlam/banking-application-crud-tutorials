package com.shah.bankingapplicationcrud.model.response;

import com.shah.bankingapplicationcrud.exception.CrudError;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.FAIL;
import static com.shah.bankingapplicationcrud.constant.CommonConstants.SUCCESS;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOneCustomerResponse {

    @Schema(description = "Possible value: \n" +
            " + SUCCESS\n" +
            " + FAILED\n")
    private String status;

    @Schema(description = "Details of customer")
    private Customer customer;

    @Schema(description = "Error description")
    private CrudError error;

    public static GetOneCustomerResponse success(Customer customer) {
        return new GetOneCustomerResponse(SUCCESS, customer, null);
    }

    public static GetOneCustomerResponse fail(Customer customer, CrudError error) {
        return new GetOneCustomerResponse(FAIL, customer, error);
    }

}
