package edu.dental.domain.entities;


import edu.dental.utils.data_structures.MyList;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

/**
 * The DentalWork class represent object of a record, containing data about certain work.
 * Implements {@link java.io.Serializable} because it is an object of information,
 *  and instances may be stored in memory.
 * Has fields {@code patient,clinic,acceptDate,complete,} {@link Product} list with a types of the products.
 * Also containing {@code byte array} with an images of the work.
 */
public class DentalWork implements I_DentalWork {

    private int id;

    private String patient;

    private String clinic;

    private MyList<Product> products;

    private LocalDate accepted;

    private LocalDate complete;

    private Status status;

    private byte[] photo;

    private String comment;

    private int reportId;


    /**
     * Constructor for data access object.
     */
    public DentalWork() {}

    private DentalWork(MyList<Product> products, LocalDate accepted, Status status) {
        this.products = products;
        this.accepted = accepted;
        this.status = status;
    }

    /**
     * Create the DentalWork object.
     */
    public static Builder create() {
        MyList<Product> products = new MyList<>();
        LocalDate accepted = LocalDate.now();
        Status s = Status.MAKE;
        return new DentalWork(products, accepted, s).new Builder();
    }

    /**
     * The builder pattern class.
     */
    public class Builder {

        private Builder(){}

        public Builder setId(int id) {
            DentalWork.this.setId(id);
            return this;
        }

        public Builder setPatient(String patient) {
            DentalWork.this.setPatient(patient);
            return this;
        }

        public Builder setClinic(String clinic) {
            DentalWork.this.setClinic(clinic);
            return this;
        }

        public Builder setComplete(LocalDate complete) {
            DentalWork.this.setComplete(complete);
            return this;
        }

        public Builder setPhoto(byte[] photo) {
            //TODO
            DentalWork.this.setPhoto(photo);
            return this;
        }

        public Builder setComment(String comment) {
            DentalWork.this.setComment(comment);
            return this;
        }

        public DentalWork build() {
            return DentalWork.this;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DentalWork that = (DentalWork) o;
        return id == that.id && Objects.equals(patient, that.patient) && Objects.equals(clinic, that.clinic)
                                && Objects.equals(complete, that.complete) && accepted.equals(that.accepted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accepted);
    }

    @Override
    public String toString() {
        return "\nDentalWork{" +
                "\n id=" + id +
                ", \n patient='" + patient + '\'' +
                ", \n clinic='" + clinic + '\'' +
                ", \n products=" + products +
                ", \n accepted=" + accepted +
                ", \n complete=" + complete +
                ", \n status=" + status +
                ", \n reportId=" + reportId +
                '}';
    }

    /*                            ||
            Getters and setters   \/
     */

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getPatient() {
        return patient;
    }

    @Override
    public void setPatient(String patient) {
        this.patient = patient == null||patient.isEmpty() ?
                LocalDate.now().toString() : patient;
    }

    @Override
    public String getClinic() {
        return clinic;
    }

    @Override
    public void setClinic(String clinic) {
        this.clinic = clinic == null||clinic.isEmpty() ? "unknown" : clinic;
    }

    @Override
    public MyList<Product> getProducts() {
        return products;
    }

    @Override
    public void setProducts(Collection<Product> products) {
        this.products = (MyList<Product>) products;
    }

    @Override
    public LocalDate getComplete() {
        return complete;
    }

    @Override
    public void setComplete(LocalDate complete) {
        this.complete = complete;
    }

    @Override
    public LocalDate getAccepted() {
        return accepted;
    }

    @Override
    public void setAccepted(LocalDate accepted) {
        this.accepted = accepted;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    @Override
    public int getReportId() {
        return reportId;
    }
}
