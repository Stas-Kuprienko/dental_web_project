package edu.dental.dto;

import edu.dental.entities.DentalWork;
import edu.dental.entities.Product;
import utils.collections.SimpleList;

import java.time.LocalDate;
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
    private int reportId;

    public DentalWorkDto(int id, String patient, String clinic, ProductDto[] products,
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

    public DentalWorkDto(DentalWork dw) {
        this.id = dw.getId();
        this.patient = dw.getPatient();
        this.clinic = dw.getClinic();
        this.accepted = dw.getAccepted().toString();
        this.complete = dw.getComplete() != null ? dw.getComplete().toString() : "";
        this.comment = dw.getComment() != null ? dw.getComment() : "";
        this.status = dw.getStatus().toString();
        this.reportId = dw.getReportId();
        this.products = new ProductDto[dw.getProducts().size()];
        dw.getProducts().stream().map(ProductDto::parse).toList().toArray(products);
    }

    public static DentalWork revert(int userId, DentalWorkDto dto) {
        DentalWork dw = new DentalWork();
        dw.setUserId(userId);
        dw.setId(dto.id);
        dw.setPatient(dto.patient);
        dw.setClinic(dto.clinic);
        dw.setComplete(dto.complete);
        dw.setAccepted(LocalDate.parse(dto.accepted));
        dw.setStatus(dto.status);
        dw.setReportId(dto.reportId);
        Product[] productArray = ProductDto.revert(dto.products);
        dw.setProducts(new SimpleList<>(productArray));
        return dw;
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
        DentalWorkDto that = (DentalWorkDto) o;
        return id == that.id && Objects.equals(patient, that.patient) &&
                Objects.equals(clinic, that.clinic) && Arrays.equals(products, that.products) &&
                Objects.equals(accepted, that.accepted) && Objects.equals(complete, that.complete) &&
                Objects.equals(comment, that.comment) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accepted);
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
