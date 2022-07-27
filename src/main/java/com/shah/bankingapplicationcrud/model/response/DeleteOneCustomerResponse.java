package com.shah.bankingapplicationcrud.model.response;

import com.shah.bankingapplicationcrud.exception.CrudError;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static com.shah.bankingapplicationcrud.model.constant.CommonConstants.FAIL;
import static com.shah.bankingapplicationcrud.model.constant.CommonConstants.SUCCESS;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteOneCustomerResponse {

    @Schema(description = "Possible value: \n" +
            " + SUCCESS\n" +
            " + FAILED\n")
    private String status;

    @Schema(description = "Details of customer")
    private UUID customerId;

    @Schema(description = "Error description")
    private CrudError error;

    public static DeleteOneCustomerResponse success(UUID id) {
        return new DeleteOneCustomerResponse(SUCCESS, id, null);
    }

    public static DeleteOneCustomerResponse fail(UUID id, CrudError error) {
        return new DeleteOneCustomerResponse(FAIL, id, error);
    }

}
