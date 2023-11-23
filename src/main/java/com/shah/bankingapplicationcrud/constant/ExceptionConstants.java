package com.shah.bankingapplicationcrud.constant;

/**
 * @author NORUL
 */
public class ExceptionConstants {

    private ExceptionConstants() {
    }

    public static final String CONSTRAINT_VIOLATION_EXCEPTION = "CONSTRAINT VIOLATION EXCEPTION";
    public static final String DATABASE_INTEGRITY_VIOLATION_EXCEPTION = "DATABASE_INTEGRITY_VIOLATION_EXCEPTION";

    public static final String CUSTOMER_EMAIL_ALREADY_EXISTS = "CUSTOMER email already exists";
    public static final String CUSTOMER_NOT_FOUND = "Customer not found";
    public static final String BAD_REQUEST = "Bad request";
}
