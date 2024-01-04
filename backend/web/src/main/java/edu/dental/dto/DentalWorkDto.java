package edu.dental.dto;

import java.util.Arrays;
import java.util.Objects;

public class DentalWorkDto {

    private final int id;
    private String patient;
    private String clinic;
    private ProductDto[] products;
    private String accepted;
    private String complete;
    private String comment;
    private String status;

    public DentalWorkDto(int id, String patient, String clinic, ProductDto[] products,
                         String accepted, String complete, String comment, String status) {
        this.id = id;
        this.patient = patient;
        this.clinic = clinic;
        this.products = products;
        this.accepted = accepted;
        this.complete = complete;
        this.comment = comment;
        this.status = status;
    }

    public DentalWorkDto(edu.dental.entities.DentalWork dw) {
        this.id = dw.getId();
        this.patient = dw.getPatient();
        this.clinic = dw.getClinic();
        this.accepted = dw.getAccepted().toString();
        this.complete = dw.getComplete() != null ? dw.getComplete().toString() : "";
        this.comment = dw.getComment() != null ? dw.getComment() : "";
        this.status = dw.getStatus().toString();
        this.products = new ProductDto[dw.getProducts().size()];
        dw.getProducts().stream().map(ProductDto::parse).toList().toArray(products);
    }

    public int getId() {
        return id;
    }

    public String getPatient() {
        return patient;
    }

    public String getClinic() {
        return clinic;
    }

    public ProductDto[] getProducts() {
        return products;
    }

    public String getAccepted() {
        return accepted;
    }

    public String getComplete() {
        return complete;
    }

    public String getComment() {
        return comment;
    }

    public String getStatus() {
        return status;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public void setProducts(ProductDto[] products) {
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

    public enum Status {
        MAKE, CLOSED, PAID
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DentalWorkDto that = (DentalWorkDto) o;
        return id == that.id && Objects.equals(patient, that.patient) &&
                Objects.equals(clinic, that.clinic) && Arrays.equals(products, that.products) &&
                Objects.equals(accepted, that.accepted) && Objects.equals(complete, that.complete) &&
                Objects.equals(comment, that.comment) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, patient, clinic, accepted, complete, comment, status);
        result = 31 * result + Arrays.hashCode(products);
        return result;
    }

    @Override
    public String toString() {
        return "DentalWorkDto{" +
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
