package com.shah.bankingapplicationcrud.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PatchCustomerRequest extends CreateCustomerRequest {

    @NotNull(message = "accountNumber can not be null")
    private UUID accountNumber;

}
