package edu.dental.domain.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

public interface I_WorkRecord extends IDHaving, Serializable {

    void setPatient(String patient);

    String getPatient();

    void setClinic(String clinic);

    String getClinic();

    void setProducts(Collection<Product> products);

    Collection<Product> getProducts();

    void setAccepted(LocalDate accepted);

    LocalDate getAccepted();

    void setComplete(LocalDate complete);

    LocalDate getComplete();

    void setStatus(Status status);

    Status getStatus();

    enum Status {
        MAKE, COMPETED, CLOSED, PAID
    }

    void setReportId(int reportId);

    int getReportId();
}
