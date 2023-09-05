package dental.database.requests.instantiation;

import dental.app.MyList;
import dental.database.requests.PullRequest;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class TableReportDBInstantiation extends PullRequest {

    private static final String SAMPLE = "SELECT * FROM %s;";

    private TableReportDBInstantiation(String tableName) throws SQLException {
        String request = String.format(SAMPLE, tableName);
        doRequest(request);
    }

    public static String[][] requireDataArrays(String tableName) throws SQLException {
        return new TableReportDBInstantiation(tableName).buildTableData();
    }

    private String[] buildTableColumns() throws SQLException {
        ResultSetMetaData metaDataSet = this.result.getMetaData();
        int columnCount = metaDataSet.getColumnCount();
        String[] result = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            result[i - 1] = metaDataSet.getColumnName(i);
        }
        return result;
    }

    private String[][] buildTableData() throws SQLException {
        String[] columns = buildTableColumns();
        MyList<String[]> tableData = new MyList<>();
        int count = 0;
        while (this.result.next()) {
            tableData.add(new String[columns.length]);
            String[] rowArray = tableData.get(count);
            rowArray[0] = this.result.getString("patient");
            rowArray[1] = this.result.getString("clinic");
            for (int i = 2; i < columns.length; i++) {
                int valueOfColumn = result.getInt(columns[i]);
                if (valueOfColumn != 0) {
                    rowArray[i] = String.valueOf(valueOfColumn);
                } else rowArray[i] = "";
            }
            count++;
        }
        return tableData.toArray(new String[tableData.size()][]);
    }
}
