package com.mutableconst.sql;

import java.io.InvalidObjectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mutableconst.exception.NoSuchQueryParameterException;
import com.mutableconst.sql.util.SqlResult;
import com.mutableconst.sql.util.SqlUtils;

public class NamedPreparedStatement {

    private Map<Integer, String> indexParameterMap;
    private Map<String, Object> parameterMap = new HashMap<>();

    private String unparamaterizedSql;

    public NamedPreparedStatement(String preparedSql) {
        this.indexParameterMap = SqlUtils.extractParameters(preparedSql);
        this.unparamaterizedSql = SqlUtils.unparameterizeSql(preparedSql);
    }

    /**
     *
     * @param parameter
     * @param value
     * @throws InvalidObjectException
     */
    public NamedPreparedStatement setParameterValue(String parameter, Object value) throws NoSuchQueryParameterException {
        if(indexParameterMap.containsValue(parameter)) {
            parameterMap.put(parameter, value);
        } else {
            throw new NoSuchQueryParameterException("Invalid Sql Parameter: " + parameter);
        }
        return this;
    }

    /**
     * execute this prepared sql statement against the connection
     * @param connection
     * @return
     * @throws SQLException
     */
    public SqlResult execute(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = createPreparedStatement(connection);
        return new SqlResult(preparedStatement.execute(), null);
    }

    protected PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        if(indexParameterMap.size() != parameterMap.size()) {
            throw new SQLException("Parameters supplied and required to not match");
        }

        PreparedStatement preparedStatement = connection.prepareStatement(unparamaterizedSql);
        for(Integer idx : indexParameterMap.keySet()) {
            preparedStatement.setObject(idx, parameterMap.get(idx));
        }

        return preparedStatement;
    }

    public Map<Integer, String> getIndexParameterMap() {
        return indexParameterMap;
    }

    public String getUnparamaterizedSql() {
        return unparamaterizedSql;
    }
}
