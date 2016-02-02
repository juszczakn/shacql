package com.mutableconst.sql

import com.mutableconst.sql.util.SqlResult
import java.sql.Connection
import java.sql.SQLException

class NamedPreparedQuery(preparedSql: String) : NamedPreparedStatement(preparedSql) {

    @Throws(SQLException::class)
    override fun execute(connection: Connection, parameters: Map<String, Any>): SqlResult {
        val query = createPreparedStatement(connection, parameters)
        return SqlResult(true, query.executeQuery(), query)
    }
}
