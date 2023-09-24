package edu.dental.domain.entities;


import edu.dental.utils.data_structures.MyList;

import java.awt.image.BufferedImage;
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
public class WorkRecord implements Serializable, IDHaving {

    private int id;

    private String patient;

    private String clinic;

    private MyList<Product> products;

    private LocalDate accepted;

    private LocalDate complete;

    private boolean closed;

    private boolean paid;

    private BufferedImage photo;

    private String comment;


    public WorkRecord() {}

    private WorkRecord(MyList<Product> products, LocalDate accepted, boolean closed, boolean paid) {
        this.products = products;
        this.accepted = accepted;
        this.closed = closed;
        this.paid = paid;
    }

    /**
     * Create the WorkRecord object.
     */
    public static Builder create() {
        MyList<Product> products = new MyList<>();
        LocalDate accepted = LocalDate.now();
        boolean closed = false;
        boolean paid = false;
        return new WorkRecord(products, accepted, closed, paid).new Builder();
    }


    class Builder {

        private Builder(){}

        public Builder setId(int id) {
            WorkRecord.this.setId(id);
            return this;
        }

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

        public Builder setPhoto(BufferedImage photo) {
            //TODO
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkRecord that = (WorkRecord) o;
        return id == that.id && Objects.equals(patient, that.patient) && Objects.equals(clinic, that.clinic)
                                && Objects.equals(complete, that.complete) && accepted.equals(that.accepted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accepted);
    }

    @Override
    public String toString() {
        return "WorkRecord{" +
                "id=" + id +
                ", patient='" + patient + '\'' +
                ", clinic='" + clinic + '\'' +
                ", products=" + products +
                ", accepted=" + accepted +
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

    public void setProducts(MyList<Product> products) {
        this.products = products;
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

    public void setAccepted(LocalDate accepted) {
        this.accepted = accepted;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public BufferedImage getPhoto() {
        return photo;
    }

    public void setPhoto(BufferedImage photo) {
        this.photo = photo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
