package edu.dental.service;

import edu.dental.HttpWebException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class HttpRequester {

    private final String url;

    public HttpRequester(String url) {
        this.url = url;
    }


    public String sendHttpGetRequest(String jwt, String resource) throws IOException, HttpWebException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(this.url + resource);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer " + jwt);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } else {
                String message = fillMessage(connection.getURL(), responseCode, connection.getResponseMessage());
                WebAPIManager.INSTANCE.getLoggerKit()
                        .doLog(this.getClass(), message, Level.INFO);
                throw new HttpWebException(responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    public String sendHttpPostRequest(String resource, String requestBody) throws IOException, HttpWebException {
        String method = "POST";
        return sendHttpRequest(method, resource, requestBody);
    }

    public String sendHttpPostRequest(String jwt, String resource, String requestBody) throws IOException, HttpWebException {
        String method = "POST";
        return sendHttpRequest(jwt, method, resource, requestBody);
    }

    public String sendHttpPutRequest(String jwt, String resource, String requestBody) throws IOException, HttpWebException {
        String method = "PUT";
        return sendHttpRequest(jwt, method, resource, requestBody);
    }

    public String sendHttpDeleteRequest(String jwt, String resource, String requestBody) throws IOException, HttpWebException {
        String method = "DELETE";
        return sendHttpRequest(jwt, method, resource, requestBody);
    }

    public void download(String jwt, String resource, OutputStream output) throws IOException, HttpWebException {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(this.url + resource);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer " + jwt);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = new BufferedInputStream(connection.getInputStream());

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } else {
                String message = fillMessage(connection.getURL(), responseCode, connection.getResponseMessage());
                WebAPIManager.INSTANCE.getLoggerKit()
                        .doLog(this.getClass(), message, Level.INFO);
                throw new HttpWebException(responseCode);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String sendHttpRequest(String method, String resource, String requestBody) throws IOException, HttpWebException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(this.url + resource);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));
            OutputStream output = connection.getOutputStream();
            output.write(requestBodyBytes);
            output.flush();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } else {
                String message = fillMessage(connection.getURL(), responseCode, connection.getResponseMessage());
                WebAPIManager.INSTANCE.getLoggerKit()
                        .doLog(this.getClass(), message, Level.INFO);
                throw new HttpWebException(responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    private String sendHttpRequest(String jwt, String method, String resource, String requestBody) throws IOException, HttpWebException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(this.url + resource);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer " + jwt);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "UTF-8");
            OutputStream output;
            if (requestBody != null && !requestBody.isEmpty()) {
                byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
                connection.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));
                output = connection.getOutputStream();
                output.write(requestBodyBytes);
            } else {
                output = connection.getOutputStream();
            }
            output.flush();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } else {
                String message = fillMessage(connection.getURL(), responseCode, connection.getResponseMessage());
                WebAPIManager.INSTANCE.getLoggerKit()
                        .doLog(this.getClass(), message, Level.INFO);
                throw new HttpWebException(responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    private String fillMessage(URL url, int code, String response) {
        return "\n" + url.toString() +
                ";\n" + "response code = " + code +
                ";\n" + (response != null ? response : "");
    }
}
