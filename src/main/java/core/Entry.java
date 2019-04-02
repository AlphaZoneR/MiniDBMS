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
        if (!entry.has("table") || !entry.has("values")) {
            throw new RuntimeException("Invalid Entry format!");
        }

        this.table = entry.getString("table");
        this.values = new HashMap<>();

        JSONArray jsonValues = entry.getJSONArray("values");

        for (int i = 0; i < jsonValues.length(); ++i) {
            JSONObject fieldEntry = jsonValues.getJSONObject(i);

            if (!fieldEntry.has("column") || !fieldEntry.has("value")) {
                throw new RuntimeException("Invalid Entry format!");
            }

            this.values.put(entry.getString("column"), entry.getString("value"));
        }
    }

    JSONObject toJSON() {
        JSONObject result = new JSONObject();
        result.put("table", table);

        JSONArray jsonValues = new JSONArray();

        for (String key: this.values.keySet()) {
            JSONObject fieldEntry = new JSONObject();

            fieldEntry.put("column", key);
            fieldEntry.put("value", this.values.get(key));

            jsonValues.put(fieldEntry);
        }

        result.put("values", jsonValues);

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
