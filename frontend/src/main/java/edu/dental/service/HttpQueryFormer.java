package edu.dental.service;

import java.util.ArrayList;

public class HttpQueryFormer {

    private final ArrayList<Entry> entries;

    public HttpQueryFormer() {
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

    private record Entry(String key, String value) {
    }
}
