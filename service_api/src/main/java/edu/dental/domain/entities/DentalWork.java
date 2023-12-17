package edu.dental.domain.entities;


import utils.collections.SimpleList;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * The DentalWork class represent object of a record, containing data about certain work.
 * Implements {@link java.io.Serializable} because it is an object of information,
 *  and instances may be stored in memory.
 * Has fields {@code patient,clinic,acceptDate,complete,} {@link Product} list with a types of the products.
 * Also containing {@code byte array} with an images of the work.
 */
public class DentalWork implements Comparable<DentalWork>, Serializable, IDHaving {

    private int id;

    private int userId;

    private String patient;

    private String clinic;

    private SimpleList<Product> products;

    private LocalDate accepted;

    private LocalDate complete;

    private Status status;

    private String comment;

    private int reportId;


    /**
     * Constructor for data access object.
     */
    public DentalWork() {}

    private DentalWork(SimpleList<Product> products, LocalDate accepted, Status status) {
        this.products = products;
        this.accepted = accepted;
        this.status = status;
    }


    /**
     * Create the DentalWork object.
     */
    public static Builder create() {
        SimpleList<Product> products = new SimpleList<>();
        LocalDate accepted = LocalDate.now();
        Status s = Status.MAKE;
        return new DentalWork(products, accepted, s).new Builder();
    }

    @Override
    public int compareTo(DentalWork o) {
        return Integer.compare(id, o.getId());
    }


    public enum Status {
        MAKE, CLOSED, PAID
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

        public Builder setUserId(int id) {
            DentalWork.this.setUserId(id);
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
        return id == that.id && userId == that.userId &&
                Objects.equals(patient, that.patient) &&
                Objects.equals(clinic, that.clinic) &&
                Objects.equals(complete, that.complete) &&
                accepted.equals(that.accepted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accepted);
    }

    @Override
    public String toString() {
        return "\nDentalWork{" +
                "\n id=" + id +
                ", \n userId=" + userId +
                ", \n patient='" + patient + '\'' +
                ", \n clinic='" + clinic + '\'' +
                ", \n products=" + (products.isEmpty() ? "null" : products) +
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public SimpleList<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = (SimpleList<Product>) products;
    }

    public LocalDate getComplete() {
        return complete;
    }

    public void setComplete(LocalDate complete) {
        this.complete = complete;
    }

    public void setComplete(String complete) {
        this.complete = LocalDate.parse(complete);
    }

    public LocalDate getAccepted() {
        return accepted;
    }

    public void setAccepted(LocalDate accepted) {
        this.accepted = accepted;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getReportId() {
        return reportId;
    }
}
