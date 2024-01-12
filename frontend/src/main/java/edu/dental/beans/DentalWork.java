package edu.dental.beans;

import java.util.Arrays;
import java.util.Objects;

public class DentalWork {

    private final int id;
    private String patient;
    private String clinic;
    private Product[] products;
    private String accepted;
    private String complete;
    private String comment;
    private String status;
    private int reportId;

    public DentalWork(int id, String patient, String clinic, Product[] products,
                      String accepted, String complete, String comment, String status, int reportId) {
        this.id = id;
        this.patient = patient;
        this.clinic = clinic;
        this.products = products;
        this.accepted = accepted;
        this.complete = complete;
        this.comment = comment;
        this.status = status;
        this.reportId = reportId;
    }

    public int id() {
        return id;
    }

    public String patient() {
        return patient;
    }

    public String clinic() {
        return clinic;
    }

    public Product[] products() {
        return products;
    }

    public String accepted() {
        return accepted;
    }

    public String complete() {
        return complete;
    }

    public String comment() {
        return comment;
    }

    public String status() {
        return status;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public enum Status {
        MAKE, CLOSED, PAID
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DentalWork that = (DentalWork) o;
        return id == that.id && Objects.equals(patient, that.patient)
                && Objects.equals(clinic, that.clinic) && Arrays.equals(products, that.products)
                && Objects.equals(accepted, that.accepted) && Objects.equals(complete, that.complete)
                && Objects.equals(comment, that.comment) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accepted);
    }

    @Override
    public String toString() {
        return "DentalWork{" +
                "id=" + id +
                ", patient='" + patient + '\'' +
                ", clinic='" + clinic + '\'' +
                ", products=" + Arrays.toString(products) +
                ", accept='" + accepted + '\'' +
                ", complete='" + complete + '\'' +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
