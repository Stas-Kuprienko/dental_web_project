package edu.dental.domain.entities.dto;

import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.Product;

import java.util.Arrays;
import java.util.Objects;

public class DentalWorkDTO {

    private final int id;
    private final String patient;
    private final String clinic;
    private final Product[] products;
    private final String accepted;
    private final String complete;
    private final String comment;
    private final String status;

    public DentalWorkDTO(DentalWork dw) {
        this.id = dw.getId();
        this.patient = dw.getPatient();
        this.clinic = dw.getClinic();
        this.accepted = dw.getAccepted().toString();
        this.complete = dw.getComplete() != null ? dw.getComplete().toString() : null;
        this.comment = dw.getComment();
        this.status = dw.getStatus().toString();
        this.products = new Product[dw.getProducts().size()];
        dw.getProducts().toArray(products);
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

    public Product[] getProducts() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DentalWorkDTO that = (DentalWorkDTO) o;
        return id == that.id && Objects.equals(patient, that.patient) && Objects.equals(clinic, that.clinic) && Arrays.equals(products, that.products) && Objects.equals(accepted, that.accepted) && Objects.equals(complete, that.complete) && Objects.equals(comment, that.comment) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, patient, clinic, accepted, complete, comment, status);
        result = 31 * result + Arrays.hashCode(products);
        return result;
    }

    @Override
    public String toString() {
        return "DentalWorkDTO{" +
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
