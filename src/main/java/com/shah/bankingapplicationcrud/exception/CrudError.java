package com.shah.bankingapplicationcrud.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Locale;

import static com.shah.bankingapplicationcrud.exception.CrudErrorCodes.AC_INTERNAL_SERVER_ERROR;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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


    /**
     * All AcException are for 200 HTTP code.
     * errorCode and errorDesc : is used by client to take necessary biz action.
     * msErrorDetail : is used by MS side investigation/troubleshooting
     *
     * @param exp
     * @return
     */
    public static CrudError constructErrorForCrudException(CrudException exp) {
        if (nonNull(exp) && nonNull(exp.getMsErrorDesc())) {
            String errorCode = exp.getMsErrorCode().getCode(); // getCode includes:: getAppCode() + "-" + getLocalCode()
            String errorDesc = isNotBlank(exp.getMsErrorDesc()) ? exp.getMsErrorDesc() : EMPTY;
            return builder().errorCode(errorCode).description(errorDesc).build();

        } else {
            return builder().errorCode(AC_INTERNAL_SERVER_ERROR.getCode()).description(AC_INTERNAL_SERVER_ERROR.getDescription(Locale.ENGLISH)).build();
        }
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