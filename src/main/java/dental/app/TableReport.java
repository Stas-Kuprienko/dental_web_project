package dental.app;

import dental.app.records.RecordItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The Report class represent an object containing data about {@link RecordItem records} for a certain month.
 * Has the fields {@code month,year} and {@link ArrayList} with a {@link RecordItem records}.
 * Implements {@link java.io.Serializable} because it is an object of information,
 *  and instances must be storable in memory.
 * Has no methods except for getters and setters.
 */
public class TableReport implements Serializable {

    /**
     * The month of the report
     */
    private String month;

    /**
     * The year of the report
     */
    private String year;

    /**
     * The list of {@link RecordItem records} for a given month
     */
    private transient final ArrayList<RecordItem> records;

    /**
     * Create a Report object for the given month and year
     * @param month   The month for which to create report
     * @param year    The year of this report
     * @param records The {@link RecordItem recordItems} list for the given month
     */
    TableReport(String month, String year, ArrayList<RecordItem> records) {
        this.month = month;
        this.year = year;
        this.records = records;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableReport that = (TableReport) o;
        return Objects.equals(month, that.month) && Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, year);
    }

    @Override
    public String toString() {
        return "TableReport{" +
                "month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }


    /*                           ||
            Getters and setters  \/
    */

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ArrayList<RecordItem> getRecords() {
        return records;
    }
}
