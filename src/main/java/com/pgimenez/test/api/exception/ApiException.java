package com.pgimenez.test.api.exception;

public class ApiException extends Exception {
    
    private final String code;
    private final String error;
    private final Exception exception;

    public ApiException(String code, String error, Exception exception) {
        super(code, exception);
        this.code = code;
        this.error = error;
        this.exception = exception;
    }

    public String getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public Exception getException() {
        return exception;
    }

}
