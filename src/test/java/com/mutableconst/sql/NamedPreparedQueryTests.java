package com.mutableconst.sql;

import static org.testng.Assert.assertTrue;

import java.io.InvalidObjectException;
import java.sql.Connection;
import java.sql.SQLException;

import org.testng.annotations.Test;

import com.mutableconst.sql.util.SqlResult;

import mockit.Mocked;

@Test
public class NamedPreparedQueryTests {
    private final static String sqlString = "select * from users where userid = :userid and id=:id OR name=:name::varchar";

    public void testExecute_correct(@Mocked final Connection connection) throws SQLException, InvalidObjectException {
        NamedPreparedQuery namedPreparedQuery = new NamedPreparedQuery(sqlString);
        namedPreparedQuery.setParameterValue("userid", "3");
        namedPreparedQuery.setParameterValue("id", "2");
        namedPreparedQuery.setParameterValue("name", "Howdy");

        SqlResult result = namedPreparedQuery.execute(connection);
        assertTrue(result.getResultSet().isPresent());
    }

    @Test(expectedExceptions = SQLException.class)
    public void testExecute_notAllParameters_throwsException(@Mocked final Connection connection) throws SQLException, InvalidObjectException {
        NamedPreparedQuery namedPreparedQuery = new NamedPreparedQuery(sqlString);
        namedPreparedQuery.setParameterValue("userid", "3");
        namedPreparedQuery.setParameterValue("id", "2");

        namedPreparedQuery.execute(connection);
    }
}
