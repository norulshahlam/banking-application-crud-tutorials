package com.shah.bankingapplicationcrud.exception;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum CrudErrorCodes implements ErrorCode {
    /**
     * Business error(high level business specific error) - this mainly used by clients
     * AC-20000 -> AC = app or ms service name, E=error, B=backend(SOA)/M=Microservice, then 4 digits unique number.
     */

    //For global exception
    DATA_INTEGRITY_VIOLATION_EXCEPTION("10001"),
    JPA_CONNECTION_ERROR("10002"),
    CONSTRAINT_VIOLATION_EXCEPTION("10003"),


    //For 500 response error code,
    AC_INTERNAL_SERVER_ERROR("50000"),
    QUERY_CUSTOMER_FAILED("50001"),


    //======================================================================================
    /**
     * MS errors(to know more details about the error)- this mainly used for service owner for trouble shooting. Used under error info of response.
     * 200XYYY -> 200 = HTTP Error code, X=API name, YYY = 3 digits unique number
     */
    //for 200 response error code
    AC_BAD_REQUEST("20000"),
    CUSTOMER_NOT_FOUND("20001"),
    EMPTY_SOURCE_COUNTRY("20002"),
    EMPTY_CORRELATION_ID("20003"),
    EMPTY_DATE_TIME("20004"),
    INVALID_SOURCE_COUNTRY("20005"),
    INVALID_CORRELATION_ID("20006"),
    NO_CUSTOMER("20007"),
    EMPTY_ID("20008"),
    INSUFFICIENT_AMOUNT("20009"),
    PAYEE_ACCOUNT_NOT_FOUND("20010"),
    PAYER_ACCOUNT_NOT_FOUND("20011");


    private static final Map<String, String> errorDescription = new HashMap<>();

    static {
        // Global exception
        errorDescription.put("10001", "An attempt to insert or update data results in violation of an integrity constraint");
        errorDescription.put("10002", "Could not open JPA EntityManager for transaction");
        errorDescription.put("10003", "An action violates a constraint on repository structure");

        //For 500XXX error message
        errorDescription.put("50000", "Internal Server Error occurred.");
        errorDescription.put("50001", "Exception while quering customer.");

        //For 200XXX error message
        errorDescription.put("20000", "Bad/Invalid Request");
        errorDescription.put("20001", "Customer not found");
        errorDescription.put("20002", "Empty source country");
        errorDescription.put("20003", "Empty correlation id");
        errorDescription.put("20004", "Empty date time");
        errorDescription.put("20005", "Invalid source country");
        errorDescription.put("20006", "Invalid correlation id format. Eg of UUID: f9bd1139-c907-11ec-b11c-0242ac110002");
        errorDescription.put("20007", "No customer");
        errorDescription.put("20008", "No ID");
        errorDescription.put("20009", "Insufficient amount");
        errorDescription.put("20010", "Payee account not found");
        errorDescription.put("20011", "Payer account not found");
    }

    private final String appCode = "CRUD";
    protected String code;

    CrudErrorCodes(String code) {
        this.code = code;
    }

    public String getDescription(Locale locale) {
        return errorDescription.get(this.code);
    }

    public String getLocalCode() {
        return code;
    }

    public String getAppCode() {
        return this.appCode;
    }

    public String getDescription() {
        return errorDescription.get(this.code);
    }

}
