package com.shah.bankingapplicationcrud.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {

    @NotNull(message = "payerAccountNumber can not be null")
    private UUID payerAccountNumber;
    @NotNull(message = "payeeAccountNumber can not be null")
    private UUID payeeAccountNumber;
    @NotNull(message = "amount to be transferred can not be null")
    @ApiModelProperty(example = "150000.33")
    @Digits(integer = 8, fraction = 2, message = "Whole number only up to 6 digits and in 2 decimal points!")
    private BigDecimal amount;

}
