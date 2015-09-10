package com.mutableconst.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.mutableconst.sql.util.SqlResult;
import com.mutableconst.sql.util.SqlUtils;

public class NamedPreparedStatement {

    private Map<Integer, String> indexParameterMap;

    private String unparamaterizedSql;

    public NamedPreparedStatement(String preparedSql) {
        this.indexParameterMap = SqlUtils.extractParameters(preparedSql);
        this.unparamaterizedSql = SqlUtils.unparameterizeSql(preparedSql);
    }

    /**
     * execute this prepared sql statement against the connection
     * @param connection
     * @return
     * @throws SQLException
     */
    public SqlResult execute(Connection connection, Map<String, Object> parameters) throws SQLException {
        PreparedStatement preparedStatement = createPreparedStatement(connection, parameters);
        boolean success = preparedStatement.execute();
        return new SqlResult(success, null, null);
    }

    protected PreparedStatement createPreparedStatement(Connection connection, Map<String, Object> parameters) throws SQLException {
        if(indexParameterMap.size() != parameters.size() || !parameters.keySet().containsAll(indexParameterMap.values())) {
            throw new IllegalArgumentException("Parameters supplied and required do not match");
        }

        PreparedStatement preparedStatement = connection.prepareStatement(unparamaterizedSql);
        for(Integer idx : indexParameterMap.keySet()) {
            String parameterName = indexParameterMap.get(idx);
            Object parameterValue = parameters.get(parameterName);
            preparedStatement.setObject(idx, parameterValue);
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
