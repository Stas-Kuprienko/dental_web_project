package dental.database.db_statements;

import dental.database.DBManager;

import java.sql.Statement;

@FunctionalInterface
interface DBQuery {

    Statement state = DBManager.getStatement();

    boolean doQuery();

}
