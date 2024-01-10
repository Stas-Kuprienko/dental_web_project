package edu.dental;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public enum WebUtility {

    INSTANCE;

    public final String sessionUser = "user";
    public final String sessionToken = "token";

    private static final String apiUrl = "http://localhost:8080/dental-api/";

    private final HttpRequestSender requestSender;
    private final Gson parser;


    WebUtility() {
        this.requestSender = new HttpRequestSender(apiUrl);
        this.parser = new GsonBuilder().create();
    }


    public <T> String parseToJson(T object) {
        return parser.toJson(object);
    }

    public <T> T parseFromJson(String json, Class<T> clas) {
        return parser.fromJson(json, clas);
    }

    public HttpRequestSender requestSender() {
        return requestSender;
    }


    public static class HttpRequestSender {

        private final String url;

        HttpRequestSender(String url) {
            this.url = url;
        }

        public String sendHttpGetRequest(String jwt, String resource, String request) throws IOException {
            //TODO !!!
            return null;
        }

        public String sendHttpGetRequest(String jwt, String resource) throws IOException {
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
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    reader.close();
                }
            }
        }

        public String sendHttpPostRequest(String resource, String requestBody) throws IOException {
            String method = "POST";
            return sendHttpRequest(method, resource, requestBody);
        }

        public String sendHttpPostRequest(String jwt, String resource, String requestBody) throws IOException {
            String method = "POST";
            return sendHttpRequest(jwt, method, resource, requestBody);
        }

        public String sendHttpPutRequest(String jwt, String resource, String requestBody) throws IOException {
            String method = "PUT";
            return sendHttpRequest(jwt, method, resource, requestBody);
        }

        public String sendHttpDeleteRequest(String jwt, String resource, String requestBody) throws IOException {
            String method = "DELETE";
            return sendHttpRequest(jwt, method, resource, requestBody);
        }

        public void download(String jwt, String resource, OutputStream output) throws IOException {
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

        private String sendHttpRequest(String method, String resource, String requestBody) throws IOException {
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
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    reader.close();
                }
            }
        }

        private String sendHttpRequest(String jwt, String method, String resource, String requestBody) throws IOException {
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
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

    public static class QueryFormer {

        private final ArrayList<Entry> entries;

        public QueryFormer() {
            this.entries = new ArrayList<>();
        }

        public void add(String key, Object value) {
            Entry e = new Entry(key, String.valueOf(value));
            entries.add(e);
        }

        public String form() {
            if (entries.isEmpty()) {
                return "";
            } else {
                StringBuilder result = new StringBuilder();
                for (Entry e : entries) {
                    result.append(e.key).append("=").append(e.value).append("&");
                }
                result.deleteCharAt(result.length() - 1);
                return result.toString();
            }
        }

        public void reset() {
            this.entries.clear();
        }

        private record Entry(String key, String value) {}
    }
}
