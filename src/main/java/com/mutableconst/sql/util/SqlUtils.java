package com.mutableconst.sql.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class SqlUtils {
    private static final Pattern namedParamRegex = Pattern.compile("\\s*=\\s*:([A-Za-z]+)");

    public static String unparameterizeSql(String preparedSql) {
        String unparameterizedSql = namedParamRegex.matcher(preparedSql).replaceAll(" = ?");
        return unparameterizedSql;
    }

    public static Map<Integer, String> extractParameters(String preparedSql) {
        Map<Integer, String> parameterMap = Maps.newHashMap();
        Matcher namedParamMatcher = namedParamRegex.matcher(preparedSql);
        int currentParamIndex = 1;
        while(namedParamMatcher.find()) {
            parameterMap.put(currentParamIndex, namedParamMatcher.group(1));
            currentParamIndex++;
        }
        return ImmutableMap.<Integer, String>builder()
                .putAll(parameterMap)
                .build();
    }
}
