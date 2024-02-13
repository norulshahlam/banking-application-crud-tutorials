package com.shah.bankingapplicationcrud.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PatchCustomerRequest extends CreateCustomerRequest {

    @NotNull(message = "accountNumber can not be null")
    private UUID accountNumber;

}
