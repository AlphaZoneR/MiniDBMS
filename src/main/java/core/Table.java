package core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Table {
    private ArrayList<Field> fields;
    private String name;


    public Table(String name) {
        this.name = name;
        this.fields = new ArrayList<>();
    }

    public Table(String name, ArrayList<Field> fields) {
        this.name = name;
        this.fields = new ArrayList<>(fields);
    }

    public Table(JSONObject table) {
        this.name = table.getString("name");
        this.fields = new ArrayList<>();
        JSONArray fields = table.getJSONArray("fields");
        for (int i = 0; i < fields.length(); ++i) {
            JSONObject field = fields.getJSONObject(i);
            this.fields.add(new Field(field));
        }
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getFields() {
        ArrayList<String> result = this.fields.stream().map(f -> f.getName()).collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    public void addField(Field field) {
        ArrayList<Field> result = this.fields.stream().filter(o -> o.getName().equals(field.getName())).collect(Collectors.toCollection(ArrayList::new));

        if (result.size() == 0) {
            this.fields.add(field);
        }
    }

    public void addField(String name, String type) {
        ArrayList<Field> result = this.fields.stream().filter(f -> f.getName() == name).collect(Collectors.toCollection(ArrayList::new));

        if (result.size() == 0) {
            this.fields.add(new Field(name, type));
        }
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        result.put("name", this.name);

        JSONArray fields = new JSONArray();

        for (int i = 0; i < this.fields.size(); ++i) {
            fields.put(this.fields.get(i).toJSON());
        }

        result.put("fields", fields);

        return result;
    }

}
