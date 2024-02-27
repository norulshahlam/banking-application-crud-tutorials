package com.shah.bankingapplicationcrud.advice;

import com.shah.bankingapplicationcrud.exception.MyException;
import com.shah.bankingapplicationcrud.exception.Errors;
import com.shah.bankingapplicationcrud.model.response.MyResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static com.shah.bankingapplicationcrud.constant.ErrorConstants.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String ERROR_DETAIL = "requestUrl : {}, Error : {}";

    /**
     * to handle MethodArgumentNotValidException when validating request body from client input
     *
     * @param req
     * @param e
     * @return
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<MyResponse<List<Errors>>> handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException e) {

        String requestUri = req.getRequestURI();

        List<Errors> cause = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Errors.builder()
                        .fieldName(error.getField())
                        .errorMessage(error.getDefaultMessage())
                        .build())
                .toList();

        log.error(ERROR_DETAIL, requestUri, cause);
        MyResponse<List<Errors>> response = MyResponse.failureResponse(cause, "Validation failed for request URI: " + requestUri);
        return ResponseEntity.ok(response);
    }

    /**
     * When an action violates a constraint on repository structure
     *
     * @param req
     * @param e
     * @return
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseBody
    public ResponseEntity<MyResponse> handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException e) {

        List<String> errorMessages = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        log.error("requestUrl : {}, occurred an error : {}", req.getRequestURI(), errorMessages);

        MyResponse response = MyResponse.failureResponse(errorMessages, CONSTRAINT_VIOLATION_EXCEPTION);
        return ResponseEntity.ok(response);
    }

    /**
     * When an attempt to insert or update data results in violation of an integrity constraint
     *
     * @param exception
     * @return
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseBody
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String cause = exception.getCause().getCause().getMessage();

        MyResponse response = MyResponse.failureResponse(cause, DATABASE_INTEGRITY_VIOLATION_EXCEPTION);

        return ResponseEntity.ok(response);
    }


    /**
     * When sort by field but the field property is not found
     *
     * @param req
     * @param e
     * @return
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({PropertyReferenceException.class})
    @ResponseBody
    public ResponseEntity<MyResponse> handlePropertyReferenceException(HttpServletRequest req, PropertyReferenceException e) {
        String cause = e.getMessage();
        log.error(ERROR_DETAIL, req.getRequestURI(), cause);
        MyResponse response = MyResponse.failureResponse(cause, FIELD_PROPERTY_NOT_FOUND);
        return ResponseEntity.ok(response);
    }

    /**
     * User defined exception
     *
     * @param req
     * @param e
     * @return
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MyException.class})
    @ResponseBody
    public ResponseEntity<MyResponse> handleBankingException(HttpServletRequest req, MyException e) {
        String errorMessage = e.getErrorMessage();
        log.error(ERROR_DETAIL, req.getRequestURI(), errorMessage);

        MyResponse response = MyResponse.failureResponse(BAD_REQUEST, errorMessage);
        return ResponseEntity.ok(response);
    }


    /**
     * For handling CannotCreateTransactionException happened during database connectivity exception
     *
     * @param exception
     * @return
     */

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({CannotCreateTransactionException.class})
    @ResponseBody
    public ResponseEntity<MyResponse> handleCannotCreateTransactionException(CannotCreateTransactionException exception) {
        String cause = exception.getCause().getCause().getMessage();

        MyResponse response = MyResponse.failureResponse(cause, JPA_CONNECTION_ERROR);
        return ResponseEntity.ok(response);
    }

    /**
     * For all other unexpected exceptions
     *
     * @param req
     * @param e
     * @return
     */

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<MyResponse> handleBaseException(HttpServletRequest req, Exception e) {
        String cause = e.getMessage();
        log.error(ERROR_DETAIL, req.getRequestURI(), cause);

        MyResponse response = MyResponse.failureResponse(cause);
        return ResponseEntity.ok(response);
    }
}


