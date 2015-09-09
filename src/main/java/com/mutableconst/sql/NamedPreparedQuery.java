package com.mutableconst.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mutableconst.sql.util.SqlResult;

public class NamedPreparedQuery extends NamedPreparedStatement {

    public NamedPreparedQuery(String preparedSql) {
        super(preparedSql);
    }

    @Override
    public SqlResult execute(Connection connection) throws SQLException {
        PreparedStatement query = createPreparedStatement(connection);
        return new SqlResult(true, query.executeQuery());
    }
}
