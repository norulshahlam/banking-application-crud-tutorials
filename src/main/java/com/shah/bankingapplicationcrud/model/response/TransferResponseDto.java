package com.shah.bankingapplicationcrud.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponseDto {

    private String senderId;
    private String senderFirstName;
    private String senderAccOldBal;
    private String senderAccNewBal;

    private String receiverId;
    private String receiverFirstName;
    private String receiverAccOldBal;
    private String receiverAccNewBal;

    private BigDecimal amount;
    private String transactionDate;
}
