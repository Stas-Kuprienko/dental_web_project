package edu.dental.service.control;

import edu.dental.APIResponseException;
import edu.dental.beans.ProductMap;
import jakarta.servlet.http.HttpSession;

public interface ProductMapService {

    void createProductItem(ProductMap.Item item);

    void updateProductItem(int id, int value);

    void deleteProductItem(int id);
}
