# shacql
Easy SQL statements for Java, based on [yesql](https://github.com/krisajenkins/yesql).


## Purpose
Shacql is meant to be an easy way to seperate your application and database
logic and keep them in their respective domains. Shacql takes it's inspiration 
from yesql.

If you need to write SQL, write SQL. Use your SQL editor of choice, have syntax
highlighting/autocompletion.

## Usage
Shacql should be easy enough to use. Simply provide the .sql file you wish to 
generate your SQL statements from, and wala!

    -- example test.sql file
    -- name: create-test-table!
    create table test (id int not null)
    
    
    // in our Java code...
    File mySqlFile = new File("/test.sql");
    Shacql shacql = new Shacql(mySqlFile);
    shacql.execute("create-test-table!", myDbConnection);

Much like yesql, Shacql decides what to do with your query based on whether or
not you've appended an exclamation point. An exclamation point at the end of
a query name denotes that the statement is not a query. Likewise, if it is missing,
Shacql will attempt to run a query and return a wrapper around a ResultSet that 
can be used in a try-with-resources.

For example:

    try(SqlResult sqlResult = shacql.execute("select-everything", myDbConnection, myParamters)) {
        ResultSet rs = sqlResult.getResultSet().get();
        rs.next();
        ...
    } catch(Exception e) { }

## License
MIT
