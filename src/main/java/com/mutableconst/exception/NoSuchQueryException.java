package com.mutableconst.exception;

import java.util.NoSuchElementException;

public class NoSuchQueryException extends NoSuchElementException {
    private final String exceptionMessage;

    public NoSuchQueryException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
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
