package edu.dental.domain.reports.my_report_service;

import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.domain.records.ProductMap;
import edu.dental.utils.data_structures.MyList;

class DataArrayTool {

    private final ProductMap productMap;
    private final MyList<WorkRecord> recordList;

    DataArrayTool(ProductMap productMap, MyList<WorkRecord> recordList) {
        this.productMap = productMap;
        this.recordList = recordList;
    }

    /**
     * Create a table head by the product titles.
     * @return String array with column titles.
     */
    private String[] buildTableColumns() {
        String[] productTypes = productMap.keysToArray();
        String[] columns = new String[productTypes.length + 2];
        columns[0] = "patient";
        columns[1] = "clinic";
        int i = 2;
        for (String s : productTypes) {
            columns[i] = s;
            i++;
        }
        return columns;
    }

    public String[][] buildTable() {
        String[] columns = buildTableColumns();
        String[][] result = new String[recordList.size() + 1][columns.length];

        //put head of the table
        result[0] = columns;

        //counting rows of the table
        int r = 1;
        for (WorkRecord wr : recordList) {
            String[] tableRow = new String[columns.length];
            tableRow[0] = wr.getPatient();
            tableRow[1] = wr.getClinic();

            //looping product list if it exists
            if (!(wr.getProducts().isEmpty())) {
                for (Product p : wr.getProducts()) {

                    //search index of the product column
                    for (int i = 2; i < columns.length; i++) {
                        if (p.title().equals(columns[i])) {

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
}