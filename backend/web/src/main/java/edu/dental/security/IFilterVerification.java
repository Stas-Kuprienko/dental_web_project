package edu.dental.security;

import edu.dental.WebException;
import jakarta.servlet.http.HttpServletRequest;

public interface IFilterVerification {

    int verify(HttpServletRequest request) throws WebException;
}
