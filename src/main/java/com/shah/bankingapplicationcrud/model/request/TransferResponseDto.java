package com.shah.bankingapplicationcrud.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponseDto {

    private UUID payerAccountNumber;
    private String payerFirstName;
    private BigDecimal payerOldAccBal;
    private BigDecimal payerNewAccBal;

    private UUID payeeAccountNumber;
    private String payeeFirstName;
    private BigDecimal payeeOldAccBal;
    private BigDecimal payeeNewAccBal;

    private BigDecimal amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime transactionDate;
}
