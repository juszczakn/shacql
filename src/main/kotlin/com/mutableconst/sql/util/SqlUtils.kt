package com.mutableconst.sql.util

import com.google.common.collect.ImmutableMap
import com.google.common.collect.Maps
import java.util.regex.Pattern

object SqlUtils {
    private val namedParamRegex = Pattern.compile("(?<!:):([A-Za-z]+)")

    fun unparameterizeSql(preparedSql: String): String {
        val namedSqlMatcher = namedParamRegex.matcher(preparedSql)
        val unparameterizedSql = namedSqlMatcher.replaceAll("?")
        return unparameterizedSql
    }

    fun extractParameters(preparedSql: String): Map<Int, String> {
        val parameterMap = Maps.newHashMap<Int, String>()
        val namedParamMatcher = namedParamRegex.matcher(preparedSql)
        var currentParamIndex = 1
        while (namedParamMatcher.find()) {
            parameterMap.put(currentParamIndex, namedParamMatcher.group(1))
            currentParamIndex++
        }
        return ImmutableMap.builder<Int, String>().putAll(parameterMap).build()
    }
}
