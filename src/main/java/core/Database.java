package core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Database {
    private ArrayList<Table> tables;
    private String name;

    public Database(String name) {
        this.name = name;
        this.tables = new ArrayList<>();
    }

    public Database(JSONObject database) {
        this.name = database.getString("name");
        this.tables = new ArrayList<>();

        JSONArray tables = database.getJSONArray("tables");

        for (int i = 0; i < tables.length(); ++i) {
            this.tables.add(new Table(tables.getJSONObject(i)));
        }
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        result.put("name", this.name);

        JSONArray tables = new JSONArray();

        for (Table table : this.tables) {
            tables.put(table.toJSON());
        }
        result.put("tables", tables);

        return result;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Table> getTables() {
        return new ArrayList<>(this.tables);
    }

    public void addTable(Table table) {
        this.tables.add(table);
    }
}
