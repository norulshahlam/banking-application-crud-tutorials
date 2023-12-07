package com.shah.bankingapplicationcrud.advice;

import com.shah.bankingapplicationcrud.exception.BankingException;
import com.shah.bankingapplicationcrud.model.response.BankingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.shah.bankingapplicationcrud.constant.ErrorConstants.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String ERROR_DETAIL = "requestUrl : {}, occurred an error : {}, e detail : {}";

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
    public ResponseEntity<BankingResponse> handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException e) {

        String requestURL = req.getRequestURI();
        List<String> cause = new ArrayList<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            cause.add(error.getDefaultMessage());
        }
        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            cause.add(error.getDefaultMessage());
        }
        log.error(ERROR_DETAIL, requestURL, cause, e);
        BankingResponse response = BankingResponse.failureResponse(cause, BAD_REQUEST);
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
    public ResponseEntity<BankingResponse> handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException e) {

        List<String> errorMessages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        log.error("requestUrl : {}, occurred an error : {}, exception detail : {}", req.getRequestURI(), errorMessages, e);

        BankingResponse response = BankingResponse.failureResponse(errorMessages, CONSTRAINT_VIOLATION_EXCEPTION);
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

        BankingResponse response = BankingResponse.failureResponse(cause, DATABASE_INTEGRITY_VIOLATION_EXCEPTION);

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
    public ResponseEntity<BankingResponse> handlePropertyReferenceException(HttpServletRequest req, PropertyReferenceException e) {
        String cause = e.getMessage();
        log.error(ERROR_DETAIL, req.getRequestURI(), cause, e);
        BankingResponse response = BankingResponse.failureResponse(cause, FIELD_PROPERTY_NOT_FOUND);
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
    @ExceptionHandler({BankingException.class})
    @ResponseBody
    public ResponseEntity<BankingResponse> handleBankingException(HttpServletRequest req, BankingException e) {
        String errorMessage = e.getErrorMessage();
        log.error(ERROR_DETAIL, req.getRequestURI(), errorMessage, e);

        BankingResponse response = BankingResponse.failureResponse(BAD_REQUEST, errorMessage);
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
    public ResponseEntity<BankingResponse> handleCannotCreateTransactionException(CannotCreateTransactionException exception) {
        String cause = exception.getCause().getCause().getMessage();

        BankingResponse response = BankingResponse.failureResponse(cause, JPA_CONNECTION_ERROR);
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
    public ResponseEntity<Object> handleBaseException(HttpServletRequest req, Exception e) {
        String cause = e.getMessage();
        log.error(ERROR_DETAIL, req.getRequestURI(), cause, e);

        BankingResponse response = BankingResponse.failureResponse(cause);
        return ResponseEntity.ok(response);
    }
}


