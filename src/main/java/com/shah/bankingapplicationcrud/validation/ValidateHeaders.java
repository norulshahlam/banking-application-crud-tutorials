package com.shah.bankingapplicationcrud.validation;

import com.shah.bankingapplicationcrud.exception.CrudException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

import static com.shah.bankingapplicationcrud.exception.CrudErrorCodes.*;
import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;

public class ValidateHeaders {

    public static void validateHeaders(HttpHeaders headers) {
        if (ObjectUtils.isEmpty(headers)) {
            throw new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND);
        } else if (ObjectUtils.isEmpty(headers.getFirst(X_SOURCE_COUNTRY))) {
            throw new CrudException(AC_BAD_REQUEST, EMPTY_SOURCE_COUNTRY);
        } else if (ObjectUtils.isEmpty(headers.getFirst(X_CORRELATION_ID))) {
            throw new CrudException(AC_BAD_REQUEST, EMPTY_CORRELATION_ID);
        } else if (ObjectUtils.isEmpty(headers.getFirst(X_SOURCE_DATE_TIME))) {
            throw new CrudException(AC_BAD_REQUEST, EMPTY_DATE_TIME);
        } else if (!SG.equalsIgnoreCase(headers.getFirst(X_SOURCE_COUNTRY))) {
            throw new CrudException(AC_BAD_REQUEST, INVALID_SOURCE_COUNTRY);
        } else if (!Objects.requireNonNull(headers.getFirst(X_CORRELATION_ID)).matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new CrudException(AC_BAD_REQUEST, INVALID_CORRELATION_ID);
        }
    }
}
