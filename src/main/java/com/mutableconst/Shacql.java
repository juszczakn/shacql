package com.mutableconst;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.NoSuchElementException;

import com.mutableconst.exception.NoSuchQueryException;
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
        this.connection = connection;
        sqlRunners = SqlFactory.createSqlRunners(sqlFile);
    }

    /**
     * execute the given query
     * @param name
     * @return
     * @throws SQLException
     * @throws NoSuchQueryException
     */
    public SqlResult execute(String name) throws SQLException, NoSuchQueryException {
        if(connection == null) {
            throw new NoSuchElementException("No Connection provided.");
        }
        return execute(name, connection);
    }

    /**
     * execute the given query
     * @param name
     * @param connection
     * @return
     * @throws SQLException
     * @throws NoSuchQueryException
     */
    public SqlResult execute(String name, Connection connection) throws SQLException, NoSuchQueryException {
        NamedPreparedStatement namedPreparedStatement = sqlRunners.get(name);
        if(namedPreparedStatement == null) {
            throw new NoSuchQueryException("Trying to execute sql query that does not exist with name: " + name);
        }
        return namedPreparedStatement.execute(connection);
    }
}
