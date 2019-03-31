package core;

import

import org.json.JSONObject;

import java.util.HashMap;

public class Entry {

    String table;
    HashMap<String, String> values;

    Entry(String table, String columnName, String value) {
        this.table = table;
        this.values = new HashMap<>();
    }

    JSONObject toJSON() {
        JSONObject result = new JSONObject();
        result.put("table", table);

        return result;
    }

    public void add(String fieldName, String value) {
        this.values.put(fieldName, value);
    }

    public void remove(String columnName) {

    }
}
