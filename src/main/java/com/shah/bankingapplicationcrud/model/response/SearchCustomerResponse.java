package com.shah.bankingapplicationcrud.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.shah.bankingapplicationcrud.exception.CrudError;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.FAIL;
import static com.shah.bankingapplicationcrud.constant.CommonConstants.SUCCESS;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"status", "recordCount"})
public class SearchCustomerResponse {

    @Schema(description = "Possible value: \n" + " + SUCCESS\n" + " + FAILED\n")
    private String status;

    @Schema(description = "Details of all customer")
    @JsonProperty("data")
    private Page<Customer> customers;
    @Schema(description = "Details of error")
    private CrudError error;

    public static SearchCustomerResponse success(Page<Customer> customers) {
        return new SearchCustomerResponse(SUCCESS, customers, null);
    }

    public static SearchCustomerResponse fail(CrudError error) {
        return new SearchCustomerResponse(FAIL, null, error);
    }

}
