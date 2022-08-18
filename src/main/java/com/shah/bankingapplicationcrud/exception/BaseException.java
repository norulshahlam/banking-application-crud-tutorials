package com.shah.bankingapplicationcrud.exception;

import lombok.NonNull;

import java.util.Arrays;
import java.util.Locale;

public class BaseException extends RuntimeException {
    @NonNull
    private final ErrorCode errorCode;
    private final ErrorInfo errorInfo;

    protected BaseException(@NonNull ErrorCode errorCode, String message, ErrorInfo errorInfo, Locale locale, Throwable cause, Object[] params) {
        super(getMessage(errorCode, message, locale, params), cause);
        if (errorCode == null) {
            throw new NullPointerException("errorCode is marked non-null but is null");
        } else {
            this.errorCode = errorCode;
            this.errorInfo = errorInfo;
        }
    }

    protected BaseException(@NonNull ErrorCode errorCode, String errorDesc, ErrorInfo errorInfo) {
        this(errorCode, errorDesc, errorInfo, null, null, null);
        if (errorCode == null) {
            throw new NullPointerException("errorCode is marked non-null but is null");
        }
    }

    protected BaseException(@NonNull ErrorCode errorCode) {
        this(errorCode, null, null, null, null, null);
        if (errorCode == null) {
            throw new NullPointerException("errorCode is marked non-null but is null");
        }
    }

    protected BaseException(@NonNull ErrorCode errorCode, Object[] params) {
        this(errorCode, null, null, null, null, params);
        if (errorCode == null) {
            throw new NullPointerException("errorCode is marked non-null but is null");
        }
    }

    protected BaseException(@NonNull ErrorCode errorCode, String message) {
        this(errorCode, message, null, null, null, null);
        if (errorCode == null) {
            throw new NullPointerException("errorCode is marked non-null but is null");
        }
    }

    private static String getMessage(@NonNull ErrorCode errorCode, String message, Locale locale, Object[] params) {
        if (errorCode == null) {
            throw new NullPointerException("errorCode is marked non-null but is null");
        } else {
            if (locale == null) {
                locale = Locale.ENGLISH;
            }
            String description = errorCode.getDescription(locale);
            if (params != null) {
                Object[] var5 = params;
                int var6 = params.length;
                for (int var7 = 0; var7 < var6; ++var7) {
                    Object param = var5[var7];
                    description = description.replaceFirst("\\{}", "" + param);
                }
            }
            return message == null ? description : message;
        }
    }

    public static BaseException wrap(Throwable exception, @NonNull ErrorCode errorCode, Locale locale) {
        if (errorCode == null) {
            throw new NullPointerException("errorCode is marked non-null but is null");
        } else if (exception instanceof BaseException) {
            BaseException se = (BaseException) exception;
            return errorCode != se.getErrorCode() ? new BaseException(errorCode, null, se.getErrorInfo(), locale, exception, null) : se;
        } else {
            return new BaseException(errorCode, null, null, locale, exception, null);
        }
    }

    public static BaseException wrap(Throwable exception, @NonNull ErrorCode errorCode) {
        if (errorCode == null) {
            throw new NullPointerException("errorCode is marked non-null but is null");
        } else {
            return wrap(exception, errorCode, Locale.ENGLISH);
        }
    }

    public static BaseException.BaseExceptionBuilder create() {
        return new BaseException.BaseExceptionBuilder();
    }

    public String toString() {
        return "{\"errorCode\":\"" + this.getErrorCode() + "\", \"errorDesc\":\"" + this.getMessage() + "\", \"errorInfo\":" + this.getErrorInfo() + "}";
    }

    @NonNull
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public ErrorInfo getErrorInfo() {
        return this.errorInfo;
    }

    public static class BaseExceptionBuilder {
        private ErrorCode errorCode;
        private String message;
        private ErrorInfo errorInfo;
        private Locale locale;
        private Throwable cause;
        private Object[] params;

        BaseExceptionBuilder() {
        }

        public BaseException.BaseExceptionBuilder errorCode(@NonNull ErrorCode errorCode) {
            if (errorCode == null) {
                throw new NullPointerException("errorCode is marked non-null but is null");
            } else {
                this.errorCode = errorCode;
                return this;
            }
        }

        public BaseException.BaseExceptionBuilder message(String message) {
            this.message = message;
            return this;
        }

        public BaseException.BaseExceptionBuilder errorInfo(ErrorInfo errorInfo) {
            this.errorInfo = errorInfo;
            return this;
        }

        public BaseException.BaseExceptionBuilder locale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public BaseException.BaseExceptionBuilder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public BaseException.BaseExceptionBuilder params(Object[] params) {
            this.params = params;
            return this;
        }

        public BaseException build() {
            return new BaseException(this.errorCode, this.message, this.errorInfo, this.locale, this.cause, this.params);
        }

        public String toString() {
            return "BaseException.BaseExceptionBuilder(errorCode=" + this.errorCode + ", message=" + this.message + ", errorInfo=" + this.errorInfo + ", locale=" + this.locale + ", cause=" + this.cause + ", params=" + Arrays.deepToString(this.params) + ")";
        }
    }
}
