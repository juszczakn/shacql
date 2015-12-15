package com.mutableconst;

import com.google.common.collect.Maps
import com.mutableconst.exception.NoSuchQueryException
import com.mutableconst.sql.NamedPreparedStatement
import com.mutableconst.sql.SqlFactory
import com.mutableconst.sql.util.SqlResult
import java.io.File
import java.sql.Connection
import java.sql.SQLException
import java.util.*

/**
 * A mapper for SQL statements that can be executed,
 * provided via a File on initialization.

 * Each SQL statement to be mapped must provide a
 * name comment as documented as such: "-- name:"
 */
public class Shacql(sqlFile: File) {
    private val sqlRunners: Map<String, NamedPreparedStatement>

    init {
        sqlRunners = SqlFactory.createSqlRunners(sqlFile)
    }

    /**
     * Execute the given SQL statement.
     * Only to be used when the statement
     * takes no parameters.
     */
    @Throws(SQLException::class)
    public fun execute(name: String, connection: Connection): SqlResult {
        val parameters = Maps.newHashMap<String, Any>()
        return execute(name, connection, parameters)
    }

    /**
     * Execute the given SQL statement.
     */
    @Throws(SQLException::class)
    public fun execute(name: String, connection: Connection, parameters: Map<String, Any> = HashMap<String, Any>()): SqlResult {
        val errorMsg = "Trying to execute sql query that does not exist with name: " + name;
        val namedPreparedStatement = sqlRunners[name] ?: throw NoSuchQueryException(errorMsg)

        return namedPreparedStatement.execute(connection, parameters)
    }
}
