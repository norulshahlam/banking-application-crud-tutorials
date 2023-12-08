package com.shah.bankingapplicationcrud.validation;

import com.shah.bankingapplicationcrud.exception.BankingException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;
import static com.shah.bankingapplicationcrud.constant.ErrorConstants.*;

public class ValidateHeaders {

    private ValidateHeaders() {
    }

    public static void validateHeaders(HttpHeaders headers) {

        List<String> headerList = new ArrayList<>();
        if (ObjectUtils.isEmpty(headers)) {
            headerList.add(EMPTY_HEADER);
        }
        if (ObjectUtils.isEmpty(headers.getFirst(X_SOURCE_DATE_TIME))) {
            headerList.add(EMPTY_DATE_TIME);
        }
        if (ObjectUtils.isEmpty(headers.getFirst(X_SOURCE_COUNTRY))) {
            headerList.add(EMPTY_SOURCE_COUNTRY);
        }
        if (ObjectUtils.isNotEmpty(headers.getFirst(X_SOURCE_COUNTRY))) {
            if (!SG.equalsIgnoreCase(headers.getFirst(X_SOURCE_COUNTRY))) {
                headerList.add(INVALID_SOURCE_COUNTRY);
            }
        }
        if (ObjectUtils.isEmpty(headers.getFirst(X_CORRELATION_ID))) {
            headerList.add(EMPTY_CORRELATION_ID);
        }
        if (ObjectUtils.isNotEmpty(headers.getFirst(X_CORRELATION_ID))) {
            if (!headers.getFirst(X_CORRELATION_ID).matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
                headerList.add(INVALID_CORRELATION_ID);
            }
        }
        if (!headerList.isEmpty()) {
            throw new BankingException(StringUtils.join(headerList, ", "));
        }
    }
}
