package dental.app.records;

import dental.app.TableReport;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

/**
 * The RecordItem class represent object of a record, containing data about certain work.
 * Implements {@link java.io.Serializable} because it is an object of information,
 *  and instances must be stored in memory.
 * Has fields {@code patient,clinic,acceptDate,complete,} {@link Work} array with a types of the work.
 * Also can to add {@code images} array with an images of the work.
 */
public class RecordItem implements Serializable {

    /**
     * The name or surname of the patient
     */
    private String patient;

    /**
     * The clinic is consumer of the work
     */
    private String clinic;

    /**
     * The {@link Work} objects containing type, price and number of the items
     */
    private Work[] works;

    /**
     * The completion date of the work
     */
    private LocalDate complete;

    /**
     * The acceptance date of the work
     */
    private final LocalDate accepted;

    /**
     * Is the work completed? If so, this record will be added to a {@link TableReport report}.
     */
    private boolean isCompleted;


    /**
     * Create the RecordItem object.
     * @param patient  The patient name or surname
     * @param clinic   The clinic is a consumer this RecordPoint
     * @param works    The array of the {@link Work} objects of this record.
     * @param complete The completion date of the work
     */
    RecordItem(String patient, String clinic, Work[] works, LocalDate complete) {
        this.patient = patient;
        this.clinic = clinic;
        this.works = works;
        this.complete = complete;
        this.accepted = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordItem that = (RecordItem) o;
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
        return "RecordItem{" + "patient='" + patient + '\'' +
                ", clinic='" + clinic + '\'' +
                ", works=" + Arrays.toString(works) +
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

    public Work[] getWorks() {
        return works;
    }

    public void setWorks(Work[] works) {
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

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
