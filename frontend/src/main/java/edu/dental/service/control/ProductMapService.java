package edu.dental.service.control;

import edu.dental.APIResponseException;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface ProductMapService {

    void createProductItem(HttpSession session, String title, int price) throws IOException, APIResponseException;

    void updateProductItem(HttpSession session, int id, String title, int price) throws IOException, APIResponseException;

    void deleteProductItem(HttpSession session, int id, String title) throws IOException, APIResponseException;
}
