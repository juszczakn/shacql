package com.mutableconst.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;

public class SqlFactory {
    private static final Pattern nameRegex = Pattern.compile("^\\s*--[\\s-]*name:\\s*(\\S+)");
    private static final String exclamation = "!";

    /**
     * create map of names to sql runner
     * @param file
     * @return
     * @throws IOException
     */
    public static Map<String, NamedPreparedStatement> createSqlRunners(File file) throws IOException {
        Map<String, StringBuilder> sqlRunnerMap = createStringBuilders(file);
        Map<String, NamedPreparedStatement> sqlRunners = new HashMap<>();
        for(Map.Entry<String, StringBuilder> sqlRunner : sqlRunnerMap.entrySet()) {
            NamedPreparedStatement namedPreparedStatement = createSqlRunner(sqlRunner.getKey(), sqlRunner.getValue().toString());
            sqlRunners.put(sqlRunner.getKey(), namedPreparedStatement);
        }
        return ImmutableMap.<String, NamedPreparedStatement>builder()
                .putAll(sqlRunners)
                .build();
    }

    private static NamedPreparedStatement createSqlRunner(String name, String sql) {
        NamedPreparedStatement namedPreparedStatement;
        if(name.endsWith(exclamation)) {
            namedPreparedStatement = new NamedPreparedStatement(sql);
        } else {
            namedPreparedStatement = new NamedPreparedQuery(sql);
        }
        return namedPreparedStatement;
    }

    private static Map<String, StringBuilder> createStringBuilders(File file) throws IOException {
        Map<String, StringBuilder> stringBuilders = new HashMap<>();

        BufferedReader bf = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = bf.readLine()) != null) {
            Matcher matchName = nameRegex.matcher(line);
            if(matchName.find()) {
                sb = new StringBuilder();
                String name = matchName.group(1).trim();
                stringBuilders.put(name, sb);
            }
            sb.append("\n").append(line);
        }

        return stringBuilders;
    }
}
