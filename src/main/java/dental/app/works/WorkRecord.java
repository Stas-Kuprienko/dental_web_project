package dental.app.works;

import dental.app.MyList;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The WorkRecord class represent object of a record, containing data about certain work.
 * Implements {@link java.io.Serializable} because it is an object of information,
 *  and instances must be stored in memory.
 * Has fields {@code patient,clinic,acceptDate,complete,} {@link Product} list with a types of the work.
 * Also can to add {@code images} array with an images of the work.
 */
public class WorkRecord implements Serializable {

    private String patient;

    private String clinic;

    private final MyList<Product> products;

    private LocalDate complete;

    private final LocalDate accepted;

    private boolean closed;


    private WorkRecord() {
        this.products = new MyList<>(5);
        this.accepted = LocalDate.now();
        this.closed = false;
    }

    /**
     * Create the WorkRecord object.
     */
    static Builder create() {
        return new WorkRecord().new Builder();
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
        WorkRecord that = (WorkRecord) o;
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
        return "WorkRecord{" + "patient='" + patient + '\'' +
                ", clinic='" + clinic + '\'' +
                ", products=" + products +
                '}';
    }

    class Builder {

        private Builder(){}

        public Builder setPatient(String patient) {
            WorkRecord.this.patient = patient == null||patient.isEmpty() ?
                                      LocalDate.now().toString() : patient;
            return this;
        }

        public Builder setClinic(String clinic) {
            WorkRecord.this.clinic = clinic == null||clinic.isEmpty() ? "unknown" : clinic;
            return this;
        }

        public Builder setComplete(LocalDate complete) {
            WorkRecord.this.complete = complete;
            return this;
        }

        public WorkRecord build() {
            return WorkRecord.this;
        }

    }

    /*               ||
            Getters  \/
     */

    public String getPatient() {
        return patient;
    }

    public String getClinic() {
        return clinic;
    }

    public MyList<Product> getProducts() {
        return products;
    }

    public LocalDate getComplete() {
        return complete;
    }

    public LocalDate getAccepted() {
        return accepted;
    }

    public boolean getClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}