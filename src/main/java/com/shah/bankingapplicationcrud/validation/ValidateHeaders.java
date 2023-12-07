package com.shah.bankingapplicationcrud.validation;

import com.shah.bankingapplicationcrud.constant.ErrorConstants;
import com.shah.bankingapplicationcrud.exception.BankingException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;

public class ValidateHeaders {

    private ValidateHeaders() {
    }

    public static void validateHeaders(HttpHeaders headers) {
        if (ObjectUtils.isEmpty(headers)) {
            throw new BankingException(ErrorConstants.CUSTOMER_NOT_FOUND);
        } else if (ObjectUtils.isEmpty(headers.getFirst(X_SOURCE_COUNTRY))) {
            throw new BankingException(ErrorConstants.EMPTY_SOURCE_COUNTRY);
        } else if (ObjectUtils.isEmpty(headers.getFirst(X_CORRELATION_ID))) {
            throw new BankingException(ErrorConstants.EMPTY_CORRELATION_ID);
        } else if (ObjectUtils.isEmpty(headers.getFirst(X_SOURCE_DATE_TIME))) {
            throw new BankingException(ErrorConstants.EMPTY_DATE_TIME);
        } else if (!SG.equalsIgnoreCase(headers.getFirst(X_SOURCE_COUNTRY))) {
            throw new BankingException(ErrorConstants.INVALID_SOURCE_COUNTRY);
        } else if (!Objects.requireNonNull(headers.getFirst(X_CORRELATION_ID)).matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new BankingException(ErrorConstants.INVALID_CORRELATION_ID);
        }
    }
}
