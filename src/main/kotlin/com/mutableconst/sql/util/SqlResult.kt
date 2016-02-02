package com.mutableconst.sql.util

import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * A representation of the result of executing a SQL statement.
 */
public class SqlResult(val success: Boolean,
                       val resultSet: ResultSet?,
                       val preparedStatementToBeClosed: PreparedStatement?) : AutoCloseable {
    /**
     * SQL statement executed successfully.
     * @return
     */
    public fun wasSuccessful(): Boolean {
        return success
    }

    override fun close() {
        resultSet?.close();
        preparedStatementToBeClosed?.close()
    }
}
