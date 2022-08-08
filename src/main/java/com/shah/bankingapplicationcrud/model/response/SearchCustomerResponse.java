package com.shah.bankingapplicationcrud.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({"status","recordCount"})
public class SearchCustomerResponse {

    @Schema(description = "Possible value: \n" + " + SUCCESS\n" + " + FAILED\n")
    private String status;

    @Schema(description = "Details of all customer")
    private List<Customer> customers;
    @Schema(description = "Details of error")
    private CrudError error;


    public static SearchCustomerResponse success(List<Customer> customers) {
        return new SearchCustomerResponse(SUCCESS, customers, null);
    }

    public static SearchCustomerResponse fail(CrudError error) {
        return new SearchCustomerResponse(FAIL, null, error);
    }

}
