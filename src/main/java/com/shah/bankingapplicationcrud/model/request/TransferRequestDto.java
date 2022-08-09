package com.shah.bankingapplicationcrud.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDto {

    private String senderId;
    private String receiverId;
    private BigDecimal amount;

}
