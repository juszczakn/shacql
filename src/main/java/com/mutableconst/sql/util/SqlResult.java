package com.mutableconst.sql.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.google.common.base.Optional;

public class SqlResult implements AutoCloseable {
    private final boolean success;
    private final Optional<ResultSet> resultSet;
    private final PreparedStatement preparedStatementToBeClosed;

    public SqlResult(boolean success, ResultSet resultSet, PreparedStatement preparedStatement) {
        this.success = success;
        this.preparedStatementToBeClosed = preparedStatement;
        if(resultSet == null) {
            this.resultSet = Optional.absent();
        } else {
            this.resultSet = Optional.of(resultSet);
        }
    }

    public boolean wasSuccessful() {
        return success;
    }

    public Optional<ResultSet> getResultSet() {
        return resultSet;
    }

    @Override
    public void close() throws Exception {
        if(resultSet.isPresent()) {
            resultSet.get().close();
        }
        if(preparedStatementToBeClosed != null) {
            preparedStatementToBeClosed.close();
        }
    }
}
