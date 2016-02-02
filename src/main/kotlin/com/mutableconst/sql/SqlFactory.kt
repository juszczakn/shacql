package com.mutableconst.sql

import com.google.common.collect.ImmutableMap
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

object SqlFactory {
    private val nameRegex = Pattern.compile("^\\s*--[\\s-]*name:\\s*(\\S+)")
    private val exclamation = "!"

    /**
     * create map of names to sql runner
     */
    @Throws(IOException::class)
    public fun createSqlRunners(file: File): Map<String, NamedPreparedStatement> {
        val sqlRunnerMap = createStringBuilders(file)
        val sqlRunners = HashMap<String, NamedPreparedStatement>()
        for (sqlRunner in sqlRunnerMap.entrySet()) {
            val namedPreparedStatement = createSqlRunner(sqlRunner.getKey(), sqlRunner.getValue().toString())
            sqlRunners.put(sqlRunner.getKey(), namedPreparedStatement)
        }
        return ImmutableMap.builder<String, NamedPreparedStatement>().putAll(sqlRunners).build()
    }

    private fun createSqlRunner(name: String, sql: String): NamedPreparedStatement {
        val namedPreparedStatement: NamedPreparedStatement
        if (name.endsWith(exclamation)) {
            namedPreparedStatement = NamedPreparedStatement(sql)
        } else {
            namedPreparedStatement = NamedPreparedQuery(sql)
        }
        return namedPreparedStatement
    }

    private fun createStringBuilders(file: File): Map<String, StringBuilder> {
        val stringBuilders = HashMap<String, StringBuilder>()

        val bf = BufferedReader(FileReader(file))
        var sb = StringBuilder()
        var line: String? = bf.readLine()
        while (line != null) {
            val matchName = nameRegex.matcher(line)
            if (matchName.find()) {
                sb = StringBuilder()
                val name = matchName.group(1).trim()
                stringBuilders.put(name, sb)
            }
            sb.append("\n").append(line)
            line = bf.readLine()
        }

        return stringBuilders
    }
}
