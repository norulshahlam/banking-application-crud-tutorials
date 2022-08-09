package com.shah.bankingapplicationcrud.model.response;

import com.shah.bankingapplicationcrud.exception.CrudError;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.FAIL;
import static com.shah.bankingapplicationcrud.constant.CommonConstants.SUCCESS;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferAmountResponse {

    @Schema(description = "Possible value: \n" +
            " + SUCCESS\n" +
            " + FAILED\n")
    private String status;

    private TransferResponseDto data;

    @Schema(description = "Error description")
    private CrudError error;

    public static TransferAmountResponse success(TransferResponseDto data) {
        return new TransferAmountResponse(SUCCESS, data, null);
    }

    public static TransferAmountResponse fail(TransferResponseDto data, CrudError error) {
        return new TransferAmountResponse(FAIL, data, error);
    }
}
