package com.shah.bankingapplicationcrud.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shah.bankingapplicationcrud.model.enums.ResponseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankingResponse<T> {

    @Schema(description = "Possible value: \n" +
                          " + SUCCESS\n" +
                          " + FAILURE\n")
    @NonNull
    private ResponseStatus status;
    private T data;
    private String errorMessage;

    public static <T> BankingResponse successResponse(T data) {
        return BankingResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .data(data)
                .build();
    }

    public static <T> BankingResponse failureResponse(T data, String errorMessage) {
        return BankingResponse.builder()
                .status(ResponseStatus.FAILURE)
                .data(data)
                .errorMessage(errorMessage)
                .build();
    }

    public static BankingResponse failureResponse(String errorMessage) {
        return BankingResponse.builder()
                .status(ResponseStatus.FAILURE)
                .errorMessage(errorMessage)
                .build();
    }
}
