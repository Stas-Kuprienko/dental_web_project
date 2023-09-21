package dental.domain.works;

import java.time.LocalDate;

public abstract class AbstractRecordBook<E> {

    public abstract boolean createRecord(String patient, String clinic, String product, int quantity, LocalDate complete);

    public abstract boolean createRecord(String patient, String clinic, LocalDate complete);

    public abstract boolean addProductToRecord(int id, String product, int quantity);

    public abstract boolean deleteRecord(int id);

    public abstract E getRecord(String patient, String clinic);

    public abstract E getByID(int id);

    public abstract boolean editRecord(int id, Fields field, Object value);

    public enum Fields {

    }
}
