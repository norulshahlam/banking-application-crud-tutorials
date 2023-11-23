package com.shah.bankingapplicationcrud.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankingException extends RuntimeException{
    private final String errorMessage;
}
