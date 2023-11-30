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
    private final String accept;
    private final String complete;
    private final String comment;
    private final String status;

    public DentalWorkDTO(DentalWork dw) {
        this.id = dw.getId();
        this.patient = dw.getPatient();
        this.clinic = dw.getClinic();
        this.accept = dw.getAccepted().toString();
        this.complete = dw.getComplete().toString();
        this.comment = dw.getComment();
        this.status = dw.getStatus().toString();
        this.products = dw.getProducts().toArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DentalWorkDTO that = (DentalWorkDTO) o;
        return id == that.id && Objects.equals(patient, that.patient) && Objects.equals(clinic, that.clinic) && Arrays.equals(products, that.products) && Objects.equals(accept, that.accept) && Objects.equals(complete, that.complete) && Objects.equals(comment, that.comment) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, patient, clinic, accept, complete, comment, status);
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
                ", accept='" + accept + '\'' +
                ", complete='" + complete + '\'' +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
