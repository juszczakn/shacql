package com.mutableconst.sql;

import static org.testng.Assert.assertTrue;

import java.io.InvalidObjectException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.mutableconst.sql.util.SqlResult;

import mockit.Mocked;

@Test
public class NamedPreparedQueryTests {
    private final static String sqlString = "select * from users where userid = :userid and id=:id OR name=:name::varchar";

    public void testExecute_correct(@Mocked final Connection connection) throws SQLException, InvalidObjectException {
        NamedPreparedQuery namedPreparedQuery = new NamedPreparedQuery(sqlString);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userid", "3");
        parameters.put("id", "2");
        parameters.put("name", "Howdy");

        SqlResult result = namedPreparedQuery.execute(connection, parameters);
        assertTrue(result.getResultSet().isPresent());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExecute_notAllParameters_throwsException(@Mocked final Connection connection) throws SQLException {
        NamedPreparedQuery namedPreparedQuery = new NamedPreparedQuery(sqlString);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userid", "3");
        parameters.put("id", "2");

        namedPreparedQuery.execute(connection, parameters);
    }
}
