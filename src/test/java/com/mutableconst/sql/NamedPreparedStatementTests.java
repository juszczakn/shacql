package com.mutableconst.sql;


import static org.testng.Assert.assertEquals;

import java.util.Map;

import org.testng.annotations.Test;

import com.mutableconst.exception.NoSuchQueryParameterException;


@Test
public class NamedPreparedStatementTests {
    private final static String sqlString = "select * from users where userid = :userid and id=:id OR name=:name::varchar";

    @Test
    public void testParameterReplacement() {
        NamedPreparedStatement namedPreparedStatement = new NamedPreparedStatement(sqlString);
        String actual = namedPreparedStatement.getUnparamaterizedSql();
        assertEquals( actual.trim(), "select * from users where userid = ? and id = ? OR name = ?::varchar");
    }

    @Test
    public void testNamedParameterIndexMapping() {
        NamedPreparedStatement namedPreparedStatement = new NamedPreparedStatement(sqlString);
        Map<Integer, String> nameMap = namedPreparedStatement.getIndexParameterMap();
        assertEquals(nameMap.get(1), "userid");
        assertEquals(nameMap.get(2), "id");
        assertEquals(nameMap.get(3), "name");
    }

    @Test(expectedExceptions = NoSuchQueryParameterException.class)
    public void testTooManyParameters_throwsException() throws NoSuchQueryParameterException {
        NamedPreparedQuery namedPreparedQuery = new NamedPreparedQuery(sqlString);
        namedPreparedQuery.setParameterValue("doesNotExist", "Hello!");
    }
}
