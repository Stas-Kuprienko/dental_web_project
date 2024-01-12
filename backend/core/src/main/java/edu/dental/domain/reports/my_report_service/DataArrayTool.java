package edu.dental.domain.reports.my_report_service;

import edu.dental.entities.DentalWork;
import edu.dental.entities.Product;
import edu.dental.entities.ProfitRecord;

import java.util.List;

class DataArrayTool {

    private final String[] columns;
    private final String[][] result;

    DataArrayTool(String[] keysArray, List<DentalWork> recordList) {
        this.columns = buildReportColumns(keysArray);
        this.result = buildReportTable(recordList);
    }

    public DataArrayTool(ProfitRecord[] profitRecords) {
        this.columns = buildProfitColumns();
        this.result = buildProfitTable(profitRecords);
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

    private String[][] buildReportTable(List<DentalWork> recordList) {
        String[][] result = new String[recordList.size() + 1][columns.length];

        //put head of the table
        result[0] = columns;

        //counting rows of the table
        int r = 1;
        for (DentalWork wr : recordList) {
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

    private String[] buildProfitColumns() {
        return new String[] {"YEAR", "MONTH", "PROFIT"};
    }

    private String[][] buildProfitTable(ProfitRecord[] profitRecords) {
        String[][] result = new String[profitRecords.length + 1][columns.length];
        result[0] = columns;
        int r = 1;
        for (ProfitRecord sr : profitRecords) {
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