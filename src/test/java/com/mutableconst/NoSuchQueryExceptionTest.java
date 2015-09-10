package com.mutableconst;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.mutableconst.exception.NoSuchQueryException;

public class NoSuchQueryExceptionTest {
    @Test
    public void test() {
        String message = "Hi";
        NoSuchQueryException noSuchQueryException = new NoSuchQueryException(message);
        assertEquals(noSuchQueryException.getMessage(), message);
        assertEquals(noSuchQueryException.toString(), message);
    }
}
