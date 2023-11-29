package edu.dental.domain.reports.my_report_service;

import edu.dental.domain.entities.IDentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.SalaryRecord;
import utils.collections.SimpleList;

import java.util.Collection;

class DataArrayTool {

    private final String[] columns;
    private final String[][] result;

    DataArrayTool(String[] keysArray, SimpleList<IDentalWork> recordList) {
        this.columns = buildReportColumns(keysArray);
        this.result = buildReportTable(recordList);
    }

    public DataArrayTool(SalaryRecord[] salaries) {
        this.columns = buildSalaryColumns();
        this.result = buildSalaryTable(salaries);
    }

    /**
     * Create a table head by the product titles.
     * @return String array with column titles.
     */
    private String[] buildReportColumns(String[] map) {
        String[] columns = new String[map.length + 2];
        columns[0] = "PATIENT";
        columns[1] = "CLINIC";
        int i = 2;
        for (String s : map) {
            columns[i] = s.toUpperCase();
            i++;
        }
        return columns;
    }

    private String[][] buildReportTable(Collection<IDentalWork> recordList) {
        String[][] result = new String[recordList.size() + 1][columns.length];

        //put head of the table
        result[0] = columns;

        //counting rows of the table
        int r = 1;
        for (IDentalWork wr : recordList) {
            String[] tableRow = new String[columns.length];
            tableRow[0] = wr.getPatient();
            tableRow[1] = wr.getClinic();

            //looping product list if it exists
            if (!(wr.getProducts().isEmpty())) {
                for (Product p : wr.getProducts()) {

                    //search index of the product column
                    for (int i = 2; i < columns.length; i++) {
                        if (p.title().equalsIgnoreCase(columns[i])) {

                            //write quantity of the product items
                            tableRow[i] = String.valueOf(p.quantity());
                        } else {
                            if (tableRow[i] == null) {
                                tableRow[i] = "";
                            }
                        }
                    }
                }
            }
            result[r] = tableRow;
            r++;
        }
        return result;
    }

    private String[] buildSalaryColumns() {
        return new String[] {"YEAR", "MONTH", "SALARY"};
    }

    private String[][] buildSalaryTable(SalaryRecord[] salaries) {
        String[][] result = new String[salaries.length + 1][columns.length];
        result[0] = columns;
        int r = 1;
        for (SalaryRecord sr : salaries) {
            String[] tableRow = new String[columns.length];
            int i = 0;
            tableRow[i++] = String.valueOf(sr.year());
            tableRow[i++] = sr.month();
            tableRow[i] = String.valueOf(sr.amount());
            result[r] = tableRow;
            r++;
        }
        return result;
    }

    public String[][] getResult() {
        return this.result;
    }
}