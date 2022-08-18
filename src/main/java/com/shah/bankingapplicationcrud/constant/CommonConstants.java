package com.shah.bankingapplicationcrud.constant;

import java.util.UUID;

public class CommonConstants {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    public static final String X_SOURCE_COUNTRY = "x-source-country";
    public static final String X_CORRELATION_ID = "x-correlation-id";
    public static final String X_SOURCE_DATE_TIME = "x-source-date-time";
    public static final String SG = "SG";
    public static final String RANDOM_UUID = UUID.randomUUID().toString();

    public static final String DELETE_CUSTOMER = "/delete-customer";
    public static final String PATCH_CUSTOMER = "/patch-customer";
    public static final String CREATE_CUSTOMER = "/create-customer";
    public static final String GET_ONE_CUSTOMER = "/get-one-customer";
    public static final String GET_ALL_CUSTOMERS = "/get-all-customers";
    public static final String TRANSFER = "/transfer";
}
