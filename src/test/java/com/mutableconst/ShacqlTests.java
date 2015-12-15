package com.mutableconst;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mutableconst.exception.NoSuchQueryException;
import com.mutableconst.sql.util.SqlResult;

public class ShacqlTests {
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    private static File testFile;
    private Connection connection;
    static {
        try {
            testFile = Paths.get(ShacqlTests.class.getResource("/test.sql").toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public void setup() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class.forName(DRIVER).newInstance();
        connection = DriverManager.getConnection("jdbc:derby:memory:TestDb;create=true");
        connection.createStatement().execute("create table test (id int not null, name varchar(20))");
        connection.createStatement().execute("insert into test (id, name) values (1, 'test')");
    }

    @Test
    public void testInsertAndSelect() throws SQLException, IOException {
        Shacql shacql = new Shacql(testFile);
        shacql.execute("insert!", connection);

        SqlResult sqlResult = shacql.execute("select-everything", connection);
        ResultSet rs = sqlResult.getResultSet();
        rs.next();
        rs.next();

        assertTrue(sqlResult.wasSuccessful());
        assertEquals(rs.getInt(1), 2);
        assertEquals(rs.getString(2), "Bobby Tables");
    }

    @Test
    public void testAutoCloseResource_closesResultSet() throws Exception {
        Shacql shacql = new Shacql(testFile);
        ResultSet rs;
        try(SqlResult sqlResult = shacql.execute("select-everything", connection)) {
            rs = sqlResult.getResultSet();
            rs.next();

            assertEquals(rs.getInt(1), 1);
            assertEquals(rs.getString(2), "test");
        } catch (Exception e) {
            throw e;
        }
        assertTrue(rs.isClosed());
    }

    @Test(expectedExceptions = NoSuchQueryException.class)
    public void testNoSuchQueryExceptionThrown_ifDoesNotExist() throws IOException, SQLException {
        Shacql shacql = new Shacql(testFile);
        shacql.execute("no-such-query!", connection);
    }

    @Test
    public void parametersAreOptional() throws IOException, SQLException {
        Shacql shacql = new Shacql(testFile);
        shacql.execute("select-everything", connection);
    }
}
