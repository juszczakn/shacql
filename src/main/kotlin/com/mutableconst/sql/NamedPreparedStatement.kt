package com.mutableconst.sql

import com.mutableconst.sql.util.SqlResult
import com.mutableconst.sql.util.SqlUtils
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

open class NamedPreparedStatement(preparedSql: String) {

    val indexParameterMap: Map<Int, String>

    val unparamaterizedSql: String

    init {
        this.indexParameterMap = SqlUtils.extractParameters(preparedSql)
        this.unparamaterizedSql = SqlUtils.unparameterizeSql(preparedSql)
    }

    /**
     * execute this prepared sql statement against the connection
     * @param connection
     * *
     * @return
     * *
     * @throws SQLException
     */
    @Throws(SQLException::class)
    open fun execute(connection: Connection, parameters: Map<String, Any>): SqlResult {
        val preparedStatement = createPreparedStatement(connection, parameters)
        val success = preparedStatement.execute()
        return SqlResult(success, null, null)
    }

    @Throws(SQLException::class)
    protected fun createPreparedStatement(connection: Connection, parameters: Map<String, Any>): PreparedStatement {
        if (indexParameterMap.size != parameters.size || !parameters.keys.containsAll(indexParameterMap.values)) {
            throw IllegalArgumentException("Parameters supplied and required do not match")
        }

        val preparedStatement = connection.prepareStatement(unparamaterizedSql)
        for (idx in indexParameterMap.keys) {
            val parameterName = indexParameterMap[idx]
            val parameterValue = parameters[parameterName]
            preparedStatement.setObject(idx, parameterValue)
        }

        return preparedStatement
    }
}
