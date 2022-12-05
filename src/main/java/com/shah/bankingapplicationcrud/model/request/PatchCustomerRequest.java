package com.shah.bankingapplicationcrud.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PatchCustomerRequest extends  CreateCustomerRequest {

    private UUID id;

}
