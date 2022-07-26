package com.shah.bankingapplicationcrud.model.response;

import com.shah.bankingapplicationcrud.exception.CrudError;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.shah.bankingapplicationcrud.model.constant.CommonConstants.FAIL;
import static com.shah.bankingapplicationcrud.model.constant.CommonConstants.SUCCESS;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCustomerResponse {

    @Schema(description = "Possible value: \n" + " + SUCCESS\n" + " + FAILED\n")
    private String status;

    @Schema(description = "Details of all customer")
    private List<Customer> customers;

    private CrudError error;

    public static GetAllCustomerResponse success(List<Customer> customers) {
        return new GetAllCustomerResponse(SUCCESS, customers, null);
    }

    public static GetAllCustomerResponse fail(List<Customer> customers, CrudError error) {
        return new GetAllCustomerResponse(FAIL, customers, error);
    }

}
