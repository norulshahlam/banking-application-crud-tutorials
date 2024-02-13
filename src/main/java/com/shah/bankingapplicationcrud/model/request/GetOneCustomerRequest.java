package com.shah.bankingapplicationcrud.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

/**
 * @author NORUL
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GetOneCustomerRequest {

    @NotNull(message = "accountNumber can not be null")
    @Schema(
            description = "Customer account number",
            example = "f9bd1139-c907-11ec-b11c-0242ac110002")
    private UUID accountNumber;
}
