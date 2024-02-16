package edu.dental.control;

import stas.exceptions.HttpWebException;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface ProductMapService {

    void createProductItem(HttpSession session, String title, int price) throws IOException, HttpWebException;

    void updateProductItem(HttpSession session, int id, String title, int price) throws IOException, HttpWebException;

    void deleteProductItem(HttpSession session, int id, String title) throws IOException, HttpWebException;
}
