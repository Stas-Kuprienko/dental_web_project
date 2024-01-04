import edu.dental.database.dao.DAORequest;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.entities.DentalWork;
import edu.dental.entities.ProductMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class WorkRecordBookTest {

    private WorkRecordBook workRecordBook;
    private static final String CLEAN_WORKS = "DELETE FROM dental_work WHERE user_id = 0;";
    private static final String CLEAN_PRODUCT_MAP = "DELETE FROM product_map WHERE user_id = 0;";


    @BeforeEach
    public void setUp() throws WorkRecordBookException {
        workRecordBook = WorkRecordBook.getInstance(0);
    }

    @AfterEach
    public void cleanDatabase() throws SQLException {
        try (DAORequest request = new DAORequest()) {
            request.getStatement().addBatch(CLEAN_WORKS);
            request.getStatement().addBatch(CLEAN_PRODUCT_MAP);
            request.getStatement().executeBatch();
        }
    }

    @Test
    public void testCreateRecordWithProduct() throws WorkRecordBookException {
        String patient = "John Doe";
        String clinic = "Dental Clinic";
        String product = "tooth extraction";
        int quantity = 1;
        LocalDate complete = LocalDate.now();
        workRecordBook.addProductItem(product, 0);

        DentalWork dentalWork = workRecordBook.createRecord(patient, clinic, product, quantity, complete);

        assertNotNull(dentalWork);
        assertEquals(patient, dentalWork.getPatient());
        assertEquals(clinic, dentalWork.getClinic());
        assertEquals(complete, dentalWork.getComplete());
        assertEquals(1, dentalWork.getProducts().size());
        assertEquals(product, dentalWork.getProducts().get(0).title());
        assertEquals(quantity, dentalWork.getProducts().get(0).quantity());
    }

    @Test
    public void testCreateRecord() throws WorkRecordBookException {
        workRecordBook.addProductItem("testing", 0);
        DentalWork dentalWork = workRecordBook.createRecord("John Doe", "ABC Clinic", "testing", 1, LocalDate.now());

        assertNotNull(dentalWork);
        assertEquals("John Doe", dentalWork.getPatient());
        assertEquals("ABC Clinic", dentalWork.getClinic());
        assertEquals("testing", dentalWork.getProducts().get(0).title());
        assertEquals(1, dentalWork.getProducts().get(0).quantity());
        assertEquals(LocalDate.now(), dentalWork.getComplete());
    }

    @Test
    public void testAddProductItem() throws WorkRecordBookException {
        String title = "tooth extraction";
        int price = 100;

        ProductMap.Item item = workRecordBook.addProductItem(title, price);

        assertNotNull(item);
        assertEquals(title, item.getKey());
        assertEquals(price, item.getValue());
    }

    @Test
    public void testEditProductItem() throws WorkRecordBookException {
        String title = "tooth extraction";
        int price = 100;

        workRecordBook.addProductItem(title, price);

        int newPrice = 150;
        workRecordBook.editProductItem(title, newPrice);
        Integer updatedPrice = workRecordBook.getProductMap().get(title);

        assertNotNull(updatedPrice);
        assertEquals(newPrice, updatedPrice);
    }

    @Test
    public void testDeleteProductItem() throws WorkRecordBookException {
        String title = "Tooth Extraction";
        int price = 100;

        workRecordBook.addProductItem(title, price);

        boolean deleted = workRecordBook.deleteProductItem(title);

        assertTrue(deleted);
    }

    @Test
    public void testAddProductToRecord() throws WorkRecordBookException {
        workRecordBook.addProductItem("testing", 0);

        DentalWork dentalWork = workRecordBook.createRecord("John Doe", "ABC Clinic");

        workRecordBook.addProductToRecord(dentalWork, "testing", 2);

        assertEquals("testing", dentalWork.getProducts().get(0).title());
        assertEquals(2, dentalWork.getProducts().get(0).quantity());
    }

    @Test
    public void testRemoveProduct() throws WorkRecordBookException {
        String patient = "John Doe";
        String clinic = "Dental Clinic";
        String product = "tooth extraction";
        int quantity = 1;
        workRecordBook.addProductItem(product, 0);

        DentalWork dentalWork = workRecordBook.createRecord(patient, clinic);
        workRecordBook.addProductToRecord(dentalWork, product, quantity);

        workRecordBook.removeProduct(dentalWork, product);

        assertTrue(dentalWork.getProducts().isEmpty());
    }

    @Test
    public void testDeleteRecord() throws WorkRecordBookException {
        DentalWork dentalWork = workRecordBook.createRecord("John Doe", "ABC Clinic");

        workRecordBook.deleteRecord(dentalWork);

        List<DentalWork> records = workRecordBook.getRecords();
        assertFalse(records.contains(dentalWork));
    }

    @Test
    public void testEditRecord() throws WorkRecordBookException {
        String patient = "John Doe";
        String clinic = "Dental Clinic";
        String field = "patient";
        String value = "Jane Smith";

        DentalWork dentalWork = workRecordBook.createRecord(patient, clinic);

        workRecordBook.editRecord(dentalWork, field, value);

        assertEquals(value, dentalWork.getPatient());
    }

    @Test
    public void testSearchRecord() throws WorkRecordBookException {
        workRecordBook.addProductItem("cleaning", 0);
        workRecordBook.addProductItem("testing", 0);
        workRecordBook.createRecord("John Doe", "Dental Clinic 1", "cleaning", 1, LocalDate.now());
        workRecordBook.createRecord("Jane Smith", "Dental Clinic 2", "testing", 2, LocalDate.now());

        DentalWork found = workRecordBook.searchRecord("John Doe", "Dental Clinic 1");

        Assertions.assertEquals("John Doe", found.getPatient());
        Assertions.assertEquals("Dental Clinic 1", found.getClinic());

        Assertions.assertThrows(WorkRecordBookException.class, () -> workRecordBook.searchRecord("Invalid Patient", "Invalid Clinic"));
    }


}