package com.shah.bankingapplicationcrud.model.response;

import com.shah.bankingapplicationcrud.exception.CrudError;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.FAIL;
import static com.shah.bankingapplicationcrud.constant.CommonConstants.SUCCESS;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
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
        log.info("Delete customer SUCCESS...");
        return new DeleteOneCustomerResponse(SUCCESS, id, null);
    }

    public static DeleteOneCustomerResponse fail(UUID id, CrudError error) {
        log.info("Delete customer FAIL...");
        return new DeleteOneCustomerResponse(FAIL, id, error);
    }

}
