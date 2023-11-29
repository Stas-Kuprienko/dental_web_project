package edu.dental.domain.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public interface IDentalWork extends IDHaving,Comparable<IDentalWork>, Serializable {

    void setPatient(String patient);

    String getPatient();

    void setClinic(String clinic);

    String getClinic();

    void setProducts(List<Product> products);

    List<Product> getProducts();

    void setAccepted(LocalDate accepted);

    LocalDate getAccepted();

    void setComplete(LocalDate complete);

    LocalDate getComplete();

    void setStatus(Status status);

    Status getStatus();

    enum Status {
        MAKE, CLOSED, PAID
    }

    void setReportId(int reportId);

    int getReportId();

    Product findProduct(String type);
}
