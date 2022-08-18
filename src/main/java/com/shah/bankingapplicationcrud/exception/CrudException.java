package com.shah.bankingapplicationcrud.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.Objects;

@Getter
@Builder
@ToString
public class CrudException extends BaseException {

    /**
     * - All CrudException are for 200 HTTP code.
     * - errorCode and errorDesc : is used by client to take necessary biz action.
     * - msErrorCode and msErrorDesc : is used by MS side investigation/troubleshooting
     * - errorInfo : currently in AC we are not using, keeping this for future use
     **/
    private transient ErrorCode errorCode;
    private String errorDesc;
    private transient ErrorCode msErrorCode;
    private String msErrorDesc;
    private transient ErrorInfo errorInfo;

    public CrudException(ErrorCode errorCode) {
        super(errorCode == null ? CrudErrorCodes.AC_INTERNAL_SERVER_ERROR : errorCode);
        this.errorCode = errorCode == null ? CrudErrorCodes.AC_INTERNAL_SERVER_ERROR : errorCode;
    }

    public CrudException(ErrorCode errorCode, String errorDesc) {
        this(errorCode);
        this.errorDesc = errorDesc;
    }

    public CrudException(ErrorCode errorCode, String errorDesc, ErrorCode msErrorCode) {
        this(errorCode, errorDesc);
        this.msErrorCode = msErrorCode;
        if (Objects.nonNull(msErrorCode)) {
            this.msErrorDesc = msErrorCode.getDescription(Locale.ENGLISH);
        }
    }

    public CrudException(ErrorCode errorCode, ErrorCode msErrorCode) {
        this(errorCode);
        this.msErrorCode = msErrorCode;
        if (Objects.nonNull(msErrorCode)) {
            this.msErrorDesc = msErrorCode.getDescription(Locale.ENGLISH);
        }
    }

    public CrudException(ErrorCode errorCode, String errorDesc, ErrorCode msErrorCode, String msErrorDesc, ErrorInfo errorInfo) {
        super(errorCode == null ? CrudErrorCodes.AC_INTERNAL_SERVER_ERROR : errorCode, errorDesc, errorInfo);
        this.msErrorCode = msErrorCode;
        if (StringUtils.isNotBlank(msErrorDesc)) {
            this.msErrorDesc = msErrorDesc;
        } else if (Objects.nonNull(msErrorCode)) {
            this.msErrorDesc = msErrorCode.getDescription(Locale.ENGLISH);
        }
    }

    public CrudException(ErrorCode errorCode, String errorDesc, ErrorInfo errorInfo) {
        super(errorCode == null ? CrudErrorCodes.AC_INTERNAL_SERVER_ERROR : errorCode, errorDesc, errorInfo);
    }

    public CrudException(ErrorCode errorCode, ErrorCode msErrorCode, ErrorInfo errorInfo) {
        super(errorCode == null ? CrudErrorCodes.AC_INTERNAL_SERVER_ERROR : errorCode, null, errorInfo);
        this.msErrorCode = msErrorCode;
        if (Objects.nonNull(msErrorCode)) {
            this.msErrorDesc = msErrorCode.getDescription(Locale.ENGLISH);
        }
    }
}
