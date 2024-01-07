package edu.dental.service.tools;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class RequestReader {

    private final HttpServletRequest request;
    private final HashMap<String, String> parameterMap;

    public RequestReader(HttpServletRequest request) {
        this.request = request;
        this.parameterMap = readParameters();
    }

    private HashMap<String, String> readParameters() {
        HashMap<String, String> parameterMap = new HashMap<>();
        try (BufferedReader reader = request.getReader()) {

            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            String[] parameters = requestBody.toString().split("&");
            for (String parameter : parameters) {

                String[] keyValue = parameter.split("=");

                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    parameterMap.put(key, value);
                }
            }
        } catch (IOException e) {
            return new HashMap<>();
        }
        return parameterMap;
    }

    public HashMap<String, String> getParameterMap() {
        return parameterMap;
    }
}
