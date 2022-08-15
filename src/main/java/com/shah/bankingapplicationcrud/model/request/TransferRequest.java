package com.shah.bankingapplicationcrud.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {


    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "Value must be in UUID format eg: ac757ede-17a6-11ed-b14c-0242ac110002 ")
    @NotBlank
    private String payerAccountNumber;
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "Value must be in UUID format eg: ac757ede-17a6-11ed-b14c-0242ac110002 ")
    @NotBlank
    private String payeeAccountNumber;
    @NotNull
    @ApiModelProperty(example = "150000.33")
    @Digits(integer = 8, fraction = 2, message = "Whole number only up to 6 digits and in 2 decimal points!")
    private BigDecimal amount;

}