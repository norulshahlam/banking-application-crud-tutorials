package com.shah.bankingapplicationcrud.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GetOneCustomerRequest {

    @NotNull(message = "accountNumber can not be null")
    @ApiModelProperty(
            value = "Customer account number",
            required = true,
            example = "f9bd1139-c907-11ec-b11c-0242ac110002")
    private UUID accountNumber;
}
