package com.shah.bankingapplicationcrud.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {

    @Schema(description = "Possible value: \n" +
            " + SUCCESS\n" +
            " + FAILED\n")
    private String status;
    
}
