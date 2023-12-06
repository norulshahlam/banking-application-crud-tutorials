package com.shah.bankingapplicationcrud.constant;

/**
 * @author NORUL
 */
public class ErrorConstants {

    private ErrorConstants() {
    }

    public static final String CONSTRAINT_VIOLATION_EXCEPTION = "CONSTRAINT VIOLATION EXCEPTION";
    public static final String DATABASE_INTEGRITY_VIOLATION_EXCEPTION = "DATABASE_INTEGRITY_VIOLATION_EXCEPTION";

    public static final String CUSTOMER_EMAIL_ALREADY_EXISTS = "CUSTOMER email already exists";
    public static final String CUSTOMER_NOT_FOUND = "Customer not found";
    public static final String PAYER_NOT_FOUND = "Payer not found";
    public static final String PAYEE_NOT_FOUND = "Payee not found";


    public static final String BAD_REQUEST = "Bad request";
    public static final String INSUFFICIENT_AMOUNT_FOR_PAYER = "Insufficient amount for payer";
    public static final String PAYER_AND_PAYEE_ACCOUNT_NUMBERS_ARE_THE_SAME = "Payer and payee account numbers are the same";
    public static final String FIELD_PROPERTY_NOT_FOUND = "Field property not found";
    public static final String JPA_CONNECTION_ERROR = "JPA connection error";
}
