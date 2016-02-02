package com.mutableconst.sql.util;

import static org.testng.Assert.assertEquals;

import java.util.Map;

import org.testng.annotations.Test;

public class SqlUtilsTest {
    @Test
    public void testReplacingVars() {
        String sql = ":id =:id::int (:id)";
        String actual = SqlUtils.INSTANCE.unparameterizeSql(sql);
        assertEquals(actual, "? =?::int (?)");
    }

    @Test
    public void testCreatingVarMap() {
        String sql = ":id =:user::int (:name)";
        Map<Integer, String> actual = SqlUtils.INSTANCE.extractParameters(sql);
        assertEquals(actual.size(), 3);
        assertEquals(actual.get(1), "id");
        assertEquals(actual.get(2), "user");
        assertEquals(actual.get(3), "name");
    }
}
