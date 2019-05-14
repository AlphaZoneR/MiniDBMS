package core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

public class Entry {

    String table;
    HashMap<String, String> values;

    public Entry(String table) {
        this.table = table;
        this.values = new HashMap<>();
    }

    public Entry(JSONObject entry) {
        this.values = new HashMap<>();
        for (String key: entry.keySet()) {
            this.values.put(key, entry.get(key).toString());
        }
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();

        for (String key: this.values.keySet()) {
            result.put(key, this.values.get(key));
        }
        return result;
    }

    public void add(String fieldName, String value) {
        this.values.put(fieldName, value);
    }

    public void remove(String columnName) {
        if (this.values.containsKey(columnName)) {
            this.values.remove(columnName);
        }
    }

    public boolean has(String columnName) {
        return this.values.containsKey(columnName);
    }

    public String get(String columnName) {
        return this.values.get(columnName);
    }

    public Set<String> getKeys() {
        return this.values.keySet();
    }

    public String getTable() {
        return this.table;
    }
    public void setTable(String name) { this.table = name; }

    public HashMap<String, String> getAll() {
        return this.values;
    }

    public String toString() {
        String result = "<Entry ";

        for (String key: this.values.keySet()) {
            result += String.format("%s=%s ", key, this.values.get(key));
        }

        result += ">\n";

        return result;
    }
}
