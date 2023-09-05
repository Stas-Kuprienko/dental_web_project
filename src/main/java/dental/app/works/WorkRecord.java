package dental.app.works;

import dental.app.MyList;
import dental.app.filetools.Extractable;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The WorkRecord class represent object of a record, containing data about certain work.
 * Implements {@link java.io.Serializable} because it is an object of information,
 *  and instances may be stored in memory.
 * Has fields {@code patient,clinic,acceptDate,complete,} {@link Product} list with a types of the products.
 * Also can to add {@code images} array with an images of the work.
 */
public class WorkRecord implements Serializable, Extractable {

    private String patient;

    private String clinic;

    private final MyList<Product> products;

    private LocalDate complete;

    private final LocalDate accepted;

    private boolean closed;

    private File photo;

    private String comment;


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
        //TODO
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
            WorkRecord.this.setPatient(patient);
            return this;
        }

        public Builder setClinic(String clinic) {
            WorkRecord.this.setClinic(clinic);
            return this;
        }

        public Builder setComplete(LocalDate complete) {
            WorkRecord.this.setComplete(complete);
            return this;
        }

        public Builder setPhoto(String file) {
            //TODO
            File photo = new File(file);
            WorkRecord.this.setPhoto(photo);
            return this;
        }

        public Builder setComment(String comment) {
            WorkRecord.this.setComment(comment);
            return this;
        }

        public WorkRecord build() {
            return WorkRecord.this;
        }

    }

    /*                            ||
            Getters and setters   \/
     */

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient == null||patient.isEmpty() ?
                LocalDate.now().toString() : patient;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic == null||clinic.isEmpty() ? "unknown" : clinic;
    }

    public MyList<Product> getProducts() {
        return products;
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

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
