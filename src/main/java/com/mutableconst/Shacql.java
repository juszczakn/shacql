package com.mutableconst;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mutableconst.exception.NoSuchQueryException;
import com.mutableconst.exception.NoSuchQueryParameterException;
import com.mutableconst.sql.NamedPreparedStatement;
import com.mutableconst.sql.SqlFactory;
import com.mutableconst.sql.util.SqlResult;

public class Shacql {
    private Connection connection;
    private final Map<String, NamedPreparedStatement> sqlRunners;

    public Shacql(File sqlFile) throws IOException {
        sqlRunners = SqlFactory.createSqlRunners(sqlFile);
    }

    public Shacql(File sqlFile, Connection connection) throws IOException {
        this(sqlFile);
        this.connection = connection;
    }

    /**
     * execute the given query
     * @param name
     * @return
     * @throws SQLException
     * @throws NoSuchQueryException
     */
    public SqlResult execute(String name) throws SQLException, NoSuchQueryParameterException {
        Map<String, Object> parameters = Maps.newHashMap();
        return execute(name, parameters);
    }

    /**
     * execute the given query
     * @param name
     * @return
     * @throws SQLException
     * @throws NoSuchQueryException
     */
    public SqlResult execute(String name, Map<String, Object> parameters) throws SQLException, NoSuchQueryParameterException {
        if(connection == null) {
            throw new IllegalArgumentException("No Connection provided.");
        }
        return execute(connection, name, parameters);
    }

    /**
     * execute the given query
     * @param name
     * @param connection
     * @return
     * @throws SQLException
     * @throws NoSuchQueryException
     */
    public SqlResult execute(Connection connection, String name) throws SQLException, NoSuchQueryParameterException {
        Map<String, Object> parameters = Maps.newHashMap();
        return execute(connection, name, parameters);
    }

    /**
     * execute the given query
     * @param name
     * @param connection
     * @return
     * @throws SQLException
     * @throws NoSuchQueryException
     */
    public SqlResult execute(Connection connection, String name, Map<String, Object> parameters) throws SQLException, NoSuchQueryParameterException {
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
