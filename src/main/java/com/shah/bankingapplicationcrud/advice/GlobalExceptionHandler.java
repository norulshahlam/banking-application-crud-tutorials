package com.shah.bankingapplicationcrud.advice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shah.bankingapplicationcrud.exception.CrudErrorCodes;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.shah.bankingapplicationcrud.exception.CrudErrorCodes.AC_BUSINESS_ERROR;
import static com.shah.bankingapplicationcrud.exception.CrudErrorCodes.AC_INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * to handle MethodArgumentNotValidException exception
     *
     * @param req
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException exception) {

        String requestURL = req.getRequestURI();
        List<String> exceptionMessage = new ArrayList<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            exceptionMessage.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : exception.getBindingResult().getGlobalErrors()) {
            exceptionMessage.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        log.error("requestUrl : {}, occurred an error : {}, exception detail : {}", requestURL, exceptionMessage, exception);

        return ResponseEntity.ok(Message.message(AC_BUSINESS_ERROR, exceptionMessage));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({CannotCreateTransactionException.class})
    @ResponseBody
    public ResponseEntity<Object> handleBaseException(HttpServletRequest req, CannotCreateTransactionException exception) {
        List<String> exceptionMessage = new ArrayList<>();
        exceptionMessage.add(exception.getCause().getMessage());

        return ResponseEntity.ok(Message.message(
                AC_INTERNAL_SERVER_ERROR,
                exceptionMessage));
    }
}


@SuperBuilder
@Data
class Message {

    @JsonProperty("error")
    private ErrorObject errorObject;

    public static Message message(CrudErrorCodes acBusinessError, List<String> exceptionMessage) {

        List<CrudErrorDetails> crudErrorDetails = new ArrayList<>();
        exceptionMessage.forEach(message -> {
            CrudErrorDetails crudErrorDetail = CrudErrorDetails.builder()
                    .crudErrorCode(acBusinessError.getCode())
                    .crudErrorDescription(message)
                    .build();
            crudErrorDetails.add(crudErrorDetail);
        });

        ErrorObject er = ErrorObject.builder()
                .crudErrorDetails(crudErrorDetails)
                .errorCode(acBusinessError.getCode())
                .description(acBusinessError.getDescription())
                .build();

        return Message.builder()
                .errorObject(er)
                .build();
    }

    @Builder
    @Data
    private static class ErrorObject {
        private final String errorCode;
        private final String description;
        private final List<CrudErrorDetails> crudErrorDetails;
    }

    @Builder
    @Data
    private static class CrudErrorDetails {
        private final String crudErrorCode;
        private final String crudErrorDescription;

    }
}