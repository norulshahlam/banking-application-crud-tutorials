package com.shah.bankingapplicationcrud.exception;

import java.util.Locale;

public interface ErrorCode {
    String getLocalCode();

    String getDescription(Locale var1);

    String getAppCode();

    default String getCode() {
        return this.getAppCode() + "-" + this.getLocalCode();
    }
}