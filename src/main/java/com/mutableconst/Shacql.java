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

public class Shacql {
    private final Map<String, NamedPreparedStatement> sqlRunners;

    public Shacql(File sqlFile) throws IOException {
        sqlRunners = SqlFactory.createSqlRunners(sqlFile);
    }

    /**
     * execute the given query
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
     * execute the given query
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
