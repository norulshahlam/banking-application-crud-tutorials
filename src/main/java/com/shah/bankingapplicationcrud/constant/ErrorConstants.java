package com.shah.bankingapplicationcrud.constant;

/**
 * @author NORUL
 */
public class ErrorConstants {


    public static final String EMPTY_CORRELATION_ID = "Empty correlation id";
    public static final String EMPTY_DATE_TIME = "Empty date time";
    public static final String INVALID_SOURCE_COUNTRY = "Invalid source country";
    public static final String INVALID_CORRELATION_ID = "Invalid correlation id format. Eg of UUID: f9bd1139-c907-11ec-b11c-0242ac110002";

    private ErrorConstants() {
    }
    public static final String EMPTY_SOURCE_COUNTRY = "Empty source country";

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
