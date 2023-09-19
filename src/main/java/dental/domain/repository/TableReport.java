package dental.domain.repository;

import dental.domain.data_structures.MyList;
import dental.domain.works.WorkRecord;

import java.io.Serializable;
import java.util.Objects;

/**
 * The Report class represent an object containing data about {@link WorkRecord workRecords} for a certain month.
 * Has the fields {@code month,year} and {@link MyList} with a {@link WorkRecord workRecords}.
 * Implements {@link java.io.Serializable} because it is an object of information,
 *  and instances must be storable in memory.
 * Has no methods except for getters and setters.
 */
public class TableReport implements Serializable {

    private String month;

    private String year;

    private final MyList<WorkRecord> workRecords;

    /**
     * Create a Report object for the given month and year
     * @param month   The month for which to create report
     * @param year    The year of this report
     * @param workRecords The {@link WorkRecord recordItems} list for the given month
     */
    TableReport(String month, String year, MyList<WorkRecord> workRecords) {
        this.month = month;
        this.year = year;
        this.workRecords = workRecords;
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

    public MyList<WorkRecord> getRecords() {
        return workRecords;
    }
}
