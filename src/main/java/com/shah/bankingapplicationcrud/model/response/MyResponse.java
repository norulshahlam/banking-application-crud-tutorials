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
public class MyResponse<T> {

    @Schema(description = """ 
                              Possible value:
                              SUCCESS
                              FAILURE
                          """)
    @NonNull
    private ResponseStatus status;
    private T data;
    private String errorMessage;

    public static <T> MyResponse successResponse(T data) {
        return MyResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .data(data)
                .build();
    }

    public static <T> MyResponse failureResponse(T data, String errorMessage) {
        return MyResponse.builder()
                .status(ResponseStatus.FAILURE)
                .data(data)
                .errorMessage(errorMessage)
                .build();
    }

    public static MyResponse failureResponse(String errorMessage) {
        return MyResponse.builder()
                .status(ResponseStatus.FAILURE)
                .errorMessage(errorMessage)
                .build();
    }
}
