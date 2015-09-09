package com.mutableconst.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.mutableconst.sql.util.SqlResult;

public class NamedPreparedQuery extends NamedPreparedStatement {

    public NamedPreparedQuery(String preparedSql) {
        super(preparedSql);
    }

    @Override
    public SqlResult execute(Connection connection, Map<String, Object> parameters) throws SQLException {
        PreparedStatement query = createPreparedStatement(connection, parameters);
        return new SqlResult(true, query.executeQuery());
    }
}
