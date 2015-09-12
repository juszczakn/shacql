package com.mutableconst;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mutableconst.exception.NoSuchQueryException;
import com.mutableconst.sql.NamedPreparedStatement;
import com.mutableconst.sql.SqlFactory;
import com.mutableconst.sql.util.SqlResult;

/**
 * A mapper for SQL statements that can be executed,
 * provided via a File on initialization.
 *
 * Each SQL statement to be mapped must provide a
 * name comment as documented as such: "-- name:"
 */
public class Shacql {
    private final Map<String, NamedPreparedStatement> sqlRunners;

    public Shacql(File sqlFile) throws IOException {
        sqlRunners = SqlFactory.createSqlRunners(sqlFile);
    }

    /**
     * Execute the given SQL statement.
     * Only to be used when the statement
     * takes no parameters.
     *
     * @param name
     * @param connection
     * @return
     * @throws SQLException
     * @throws NoSuchQueryException
     */
    public SqlResult execute(String name, Connection connection) throws SQLException {
        Map<String, Object> parameters = Maps.newHashMap();
        return execute(name, connection, parameters);
    }

    /**
     * Execute the given SQL statement.
     *
     * @param name
     * @param connection
     * @return
     * @throws SQLException
     * @throws NoSuchQueryException
     */
    public SqlResult execute(String name, Connection connection, Map<String, Object> parameters) throws SQLException {
        NamedPreparedStatement namedPreparedStatement = sqlRunners.get(name);
        if(namedPreparedStatement == null) {
            throw new NoSuchQueryException("Trying to execute sql query that does not exist with name: " + name);
        }
        if(parameters == null) {
            parameters = new HashMap<>();
        }
        return namedPreparedStatement.execute(connection, parameters);
    }
}
