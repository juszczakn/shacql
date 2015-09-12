package com.mutableconst.sql;


import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import mockit.Mocked;


@Test
public class NamedPreparedStatementTests {
    private final static String sqlString = "select * from users where userid = :userid " +
            "and id=:id OR name=:name::varchar AND isNull(:test)";

    @Test
    public void testParameterReplacement() {
        NamedPreparedStatement namedPreparedStatement = new NamedPreparedStatement(sqlString);
        String actual = namedPreparedStatement.getUnparamaterizedSql();
        assertEquals( actual.trim(),
                "select * from users where userid = ? and id=? OR " +
                "name=?::varchar AND isNull(?)");
    }

    @Test
    public void testNamedParameterIndexMapping() {
        NamedPreparedStatement namedPreparedStatement = new NamedPreparedStatement(sqlString);
        Map<Integer, String> nameMap = namedPreparedStatement.getIndexParameterMap();
        assertEquals(nameMap.get(1), "userid");
        assertEquals(nameMap.get(2), "id");
        assertEquals(nameMap.get(3), "name");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTooManyParameters_throwsException(@Mocked Connection connection) throws SQLException {
        NamedPreparedQuery namedPreparedQuery = new NamedPreparedQuery(sqlString);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("doesNotExist", "Hello!");
        namedPreparedQuery.execute(connection, parameters);
    }
}
