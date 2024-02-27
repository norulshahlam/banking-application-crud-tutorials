package com.shah.bankingapplicationcrud.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author norulshahlam.mohsen
 */
@Getter
@AllArgsConstructor
public class MyException extends RuntimeException {
    private final String errorMessage;
}
