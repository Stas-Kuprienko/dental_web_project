package dental.domain.works;

import dental.domain.data_structures.MyList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The WorkRecord class represent object of a record, containing data about certain work.
 * Implements {@link java.io.Serializable} because it is an object of information,
 *  and instances may be stored in memory.
 * Has fields {@code patient,clinic,acceptDate,complete,} {@link Product} list with a types of the products.
 * Also can to add {@code images} array with an images of the work.
 */
public class WorkRecord implements Serializable {

    private int id;
    private String patient;

    private String clinic;

    private MyList<Product> products;

    private LocalDate complete;

    private final LocalDate accepted;

    private boolean closed;

    private boolean paid;

    private BufferedImage photo;

    private String comment;


    private WorkRecord() {
        this.products = new MyList<>(5);
        this.accepted = LocalDate.now();
        this.closed = false;
        this.paid = false;
    }

    /**
     * Constructor for the instantiation from database tables.
     * @param resultSet the {@link ResultSet} with table values.
     * @throws SQLException if a database access destroyed.
     */
    public WorkRecord(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.patient = resultSet.getString("patient");
        this.clinic = resultSet.getString("clinic");
        this.complete = resultSet.getDate("complete").toLocalDate();
        this.accepted = resultSet.getDate("accepted").toLocalDate();
        this.closed = resultSet.getBoolean("closed");
        this.paid = resultSet.getBoolean("paid");
        this.comment = resultSet.getString("comment");
        try {
            Blob photoBlob = resultSet.getBlob("photo");
            BufferedImage photo;
            photo = ImageIO.read(photoBlob.getBinaryStream());
            this.setPhoto(photo);
        } catch (IOException e) {
            e.printStackTrace();
            this.photo = null;
        }
        this.products = new MyList<>();
    }

    /**
     * Create the WorkRecord object.
     */
    static Builder create() {
        return new WorkRecord().new Builder();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkRecord that = (WorkRecord) o;
        //TODO
        return Objects.equals(patient, that.patient)
                && Objects.equals(clinic, that.clinic)
                && Objects.equals(accepted, that.accepted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patient, clinic, accepted);
    }

    @Override
    public String toString() {
        return "WorkRecord{" + "patient='" + patient + '\'' +
                ", clinic='" + clinic + '\'' +
                ", products=" + products +
                '}';
    }

    class Builder {

        private Builder(){}

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

    /*                            ||
            Getters and setters   \/
     */

    public int getId() {
        return id;
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

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
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

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
