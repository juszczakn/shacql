package com.mutableconst.sql.util;

import java.sql.ResultSet;

import com.google.common.base.Optional;

public class SqlResult {
    private final boolean success;
    private final Optional<ResultSet> resultSet;

    public SqlResult(boolean success, ResultSet resultSet) {
        this.success = success;
        if(resultSet == null) {
            this.resultSet = Optional.absent();
        } else {
            this.resultSet = Optional.of(resultSet);
        }
    }

    public boolean wasSuccessfull() {
        return success;
    }

    public Optional<ResultSet> getResultSet() {
        return resultSet;
    }
}
