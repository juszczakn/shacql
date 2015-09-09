package com.mutableconst.exception;

import java.io.InvalidObjectException;

public class NoSuchQueryParameterException extends InvalidObjectException {
    private final String exceptionMessage;

    public NoSuchQueryParameterException(String reason) {
        super(reason);
        this.exceptionMessage = reason;
    }

    @Override
    public String getMessage() {
        return this.exceptionMessage;
    }

    @Override
    public String toString() {
        return exceptionMessage;
    }
}
