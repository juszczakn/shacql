package com.mutableconst;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mutableconst.sql.util.SqlResult;

public class ShacqlTests {
    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";

    private static File testFile;
    private Connection connection;
    static {
        try {
            testFile = Paths.get(ShacqlTests.class.getResource("/tests/test.sql").toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public void setup() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class.forName(driver).newInstance();
        connection = DriverManager.getConnection("jdbc:derby:memory:TestDb;create=true");
    }

    @Test
    public void test() throws IOException, SQLException {
        Shacql shacql = new Shacql(testFile, connection);
        shacql.execute("create-table!");
        shacql.execute("insert!");
        SqlResult sqlResult = shacql.execute("select-everything");
        assertEquals(sqlResult.getResultSet().get().getInt(0), null);
    }
}
