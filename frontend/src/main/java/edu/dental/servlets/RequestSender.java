package edu.dental.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RequestSender {

    private final String url;

    public RequestSender(String url) {
        this.url = url;
    }

    public String sendHttpPostRequest(String resource, String requestBody) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(this.url + resource);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            byte[] requestBodyBytes = requestBody.getBytes();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "UTF-8");
            connection.setRequestProperty("Content-Length", Integer.toString(requestBodyBytes.length));
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

    public static class QueryFormer {

        private final ArrayList<Entry> entries;

        public QueryFormer() {
            this.entries = new ArrayList<>();
        }

        public void add(String key, Object value) {
            Entry e = new Entry(key, value.toString());
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

        private record Entry(String key, String value) {}
    }
}