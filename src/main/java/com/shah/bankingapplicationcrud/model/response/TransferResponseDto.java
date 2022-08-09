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

    private String senderId;
    private String senderFirstName;
    private BigDecimal senderAccOldBal;
    private BigDecimal senderAccNewBal;

    private String receiverId;
    private String receiverFirstName;
    private BigDecimal receiverAccOldBal;
    private BigDecimal receiverAccNewBal;

    private BigDecimal amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime transactionDate;
}
