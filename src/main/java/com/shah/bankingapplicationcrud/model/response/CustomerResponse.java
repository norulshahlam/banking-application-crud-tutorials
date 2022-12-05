package com.shah.bankingapplicationcrud.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shah.bankingapplicationcrud.exception.CrudError;
import com.shah.bankingapplicationcrud.model.enums.ResponseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse<T> {

    @Schema(description = "Possible value: \n" +
            " + SUCCESS\n" +
            " + FAILURE\n")
    @NonNull
    ResponseStatus status;
    private T data;
    private CrudError error;

    public static <T> CustomerResponse successResponse(T data) {
        return CustomerResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .data(data)
                .build();
    }
    public static CustomerResponse failureResponse(CrudError error) {
        return CustomerResponse.builder()
                .status(ResponseStatus.FAILURE)
                .error(error)
                .build();
    }

}
