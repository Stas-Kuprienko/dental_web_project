package dental.database.requests.reports;

import dental.database.ConnectionPool;
import dental.database.DBConfig;
import dental.database.requests.PullRequest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class TableReportDBInstantiation extends PullRequest {

    private static final String SAMPLE = "SELECT * FROM %s;";

    private final String tableName;

    private TableReportDBInstantiation(String tableName) throws SQLException {
        String request = String.format(SAMPLE, tableName);
        this.tableName = tableName;
        doRequest(request);
    }

    public static String[][] requireDataArrays(String tableName) throws SQLException {
        return new TableReportDBInstantiation(tableName).buildTableData();
    }

    private String[] buildTableColumns() throws SQLException {
        ResultSetMetaData metaDataSet = this.resultSet.getMetaData();
        int columnCount = metaDataSet.getColumnCount();
        String[] result = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            result[i - 1] = metaDataSet.getColumnName(i);
        }
        return result;
    }

    private String[][] buildTableData() throws SQLException {
        //create string array of the table head
        String[] columns = buildTableColumns();
        String[][] dataArray = new String[countRecords(tableName) + 1][];
        //set head of the table
        dataArray[0] = columns;
        //rows counting
        int count = 1;
        while (resultSet.next()) {
            dataArray[count] = new String[columns.length];
            String[] rowArray = dataArray[count];
            rowArray[0] = resultSet.getString("patient");
            rowArray[1] = resultSet.getString("clinic");
            //iterate products
            for (int i = 2; i < rowArray.length; i++) {
                int valueOfColumn = resultSet.getInt(columns[i]);
                if (valueOfColumn != 0) {
                    rowArray[i] = String.valueOf(valueOfColumn);
                } else rowArray[i] = "";
            }
            count++;
        }
        return dataArray;
    }

    private int countRecords(String tableName) throws SQLException {
        String query = String.format("SELECT COUNT(*) AS count_records FROM %s.%s;", DBConfig.DATA_BASE, tableName);
        Connection connection = ConnectionPool.get();
        ResultSet set = connection.createStatement().executeQuery(query);
        ConnectionPool.put(connection);
        connection = null;
        if (set.next()) {
            int count = set.getInt("count_records");
            set.close();
            return count;
        } else {
            set.close();
            return -1;
        }
    }
}
