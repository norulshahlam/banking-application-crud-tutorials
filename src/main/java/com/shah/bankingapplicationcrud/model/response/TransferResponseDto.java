package com.shah.bankingapplicationcrud.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponseDto {

    private String payerAccountNumber;
    private String payerFirstName;
    private BigDecimal payerOldAccBal;
    private BigDecimal payerNewAccBal;

    private String payeeAccountNumber;
    private String payeeFirstName;
    private BigDecimal payeeOldAccBal;
    private BigDecimal payeeNewAccBal;

    private BigDecimal amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime transactionDate;
}
