package com.shah.bankingapplicationcrud.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author norulshahlam.mohsen
 */
@Getter
@AllArgsConstructor
public class BankingException extends RuntimeException {
    private final String errorMessage;
}
