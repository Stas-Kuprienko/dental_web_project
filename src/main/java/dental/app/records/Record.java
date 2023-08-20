package dental.app.records;

import dental.app.MyList;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The Record class represent object of a record, containing data about certain work.
 * Implements {@link java.io.Serializable} because it is an object of information,
 *  and instances must be stored in memory.
 * Has fields {@code patient,clinic,acceptDate,complete,} {@link Work} list with a types of the work.
 * Also can to add {@code images} array with an images of the work.
 */
public class Record implements Serializable {

    private String patient;

    private String clinic;

    private MyList<Work> works;

    private LocalDate complete;

    private final LocalDate accepted;

    private boolean closed;


    /**
     * Create the Record object.
     * @param patient  The patient name or surname.
     * @param clinic   The clinic is a consumer this record.
     * @param complete The completion date of the work.
     */
    Record(String patient, String clinic, LocalDate complete) {
        this.patient = patient;
        this.clinic = clinic;
        this.works = new MyList<>(5);
        this.complete = complete;
        this.accepted = LocalDate.now();
        this.closed = false;
    }

    /**
     * Create the Record object.
     * @param patient  The patient name or surname.
     * @param clinic   The clinic is a consumer this record.
     * @param work     The {@link Work} object containing type, price and number of the items.
     * @param complete The completion date of the work.
     */
    Record(String patient, String clinic, Work work, LocalDate complete) {
        this.patient = patient;
        this.clinic = clinic;
        this.works = new MyList<>(5);
        works.add(work);
        this.complete = complete;
        this.accepted = LocalDate.now();
        this.closed = false;
    }

    /**
     * Methods that using for {@linkplain MyList#searchByString(dental.app.MyList.Searchable, String, Class) searching} objects.
     */
    public enum SearchBy implements MyList.Searchable {
        getPatient, getClinic
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record that = (Record) o;
        return Objects.equals(patient, that.patient)
                && Objects.equals(clinic, that.clinic)
                && Objects.equals(accepted, that.accepted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patient, clinic, accepted);
    }

    @Override
    public String toString() {
        return "Record{" + "patient='" + patient + '\'' +
                ", clinic='" + clinic + '\'' +
                ", works=" + works +
                '}';
    }

    /*                           ||
            Getters and setters  \/
     */

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public MyList<Work> getWorks() {
        return works;
    }

    public void setWorks(MyList<Work> works) {
        this.works = works;
    }

    public LocalDate getComplete() {
        return complete;
    }

    public void setComplete(LocalDate complete) {
        this.complete = complete;
    }

    public LocalDate getAccepted() {
        return accepted;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
