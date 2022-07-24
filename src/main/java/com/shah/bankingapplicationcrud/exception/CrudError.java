package com.shah.bankingapplicationcrud.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Objects.nonNull;

/**
 * @ClassName: Error
 * @Description: This is can be used for error info related to 2FA 3DS,
 * @Author: Norulshahlam
 * @Date: 2021/11/05
 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrudError {
    @Schema(title = "Error Code", example = "TFA9")
    public String errorCode;
    @Schema(title = "Error Description", example = "Network Error")
    public String description;
    @Schema(title = "Error Details")
    private List<CrudError.ErrorDetail> crudErrorDetails;

    /**
     * All AcException are for 200 HTTP code.
     * errorCode and errorDesc : is used by client to take necessary biz action.
     * msErrorDetail : is used by MS side investigation/troubleshooting
     *
     * @param exp
     * @return
     */
    public static CrudError constructErrorForCrudException(CrudException exp) {
        CrudError error = new CrudError();
        if (nonNull(exp) && nonNull(exp.getErrorCode())) {
            String errorCode = exp.getErrorCode().getCode(); // getCode includes:: getAppCode() + "-" + getLocalCode()
            String errorDesc = StringUtils.isNotBlank(exp.getErrorCode().getDescription(Locale.ENGLISH)) ? exp.getErrorCode().getDescription(Locale.ENGLISH) : StringUtils.EMPTY;
            ErrorDetail msErrorDetail = setupMsSpecificErrorDetail(exp);
            error.addErrorDetail(errorCode, errorDesc, msErrorDetail);
        } else {
            error.addErrorDetail(CrudErrorCodes.AC_TECHNICAL_ERROR.getCode(), CrudErrorCodes.AC_TECHNICAL_ERROR.getDescription(Locale.ENGLISH), null);
        }
        return error;
    }

    /**
     * This errorInfo is useful for MS side investigation/troubleshooting
     *
     * @param exp
     * @return
     */
    private static ErrorDetail setupMsSpecificErrorDetail(CrudException exp) {
        String msErrorCode = StringUtils.EMPTY;
        String msErrorDesc = StringUtils.EMPTY;
        if (nonNull(exp.getMsErrorCode())) {
            msErrorCode = StringUtils.trim(exp.getMsErrorCode().getLocalCode()); //here we need only localCode
        }
        if (StringUtils.isNotBlank(exp.getMsErrorDesc())) {
            msErrorDesc = StringUtils.trim(exp.getMsErrorDesc());
        }
        return ErrorDetail.builder().crudErrorCode(msErrorCode).crudErrorDescription(msErrorDesc).build();
    }

    public CrudError addErrorDetail(String errorCode, String errorDesc, ErrorDetail errorDetail) {
        this.errorCode = errorCode;
        this.description = errorDesc;
        List<CrudError.ErrorDetail> errorDetailList = this.checkAndGetErrorDetailList();
        errorDetailList.add(errorDetail);
        this.setCrudErrorDetails(errorDetailList);
        return this;
    }

    private List<CrudError.ErrorDetail> checkAndGetErrorDetailList() {
        if (CollectionUtils.isEmpty(this.crudErrorDetails)) {
            this.crudErrorDetails = new ArrayList<>();
        }
        return this.crudErrorDetails;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "AcError.ErrorDetail", title = "ErrorDetail")
    public static class ErrorDetail {
        @Schema(title = "Error Code from ms service error", example = "0120")
        public String crudErrorCode;
        @Schema(title = "Error Description for ms service error", example = "Soft Token is rejected by user")
        public String crudErrorDescription;
    }
}