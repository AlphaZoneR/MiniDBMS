package core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Table {
    private ArrayList<Field> fields;
    private String name;
    private int rowCount;


    public Table(String name) {
        this.name = name;
        this.fields = new ArrayList<>();
        this.rowCount = 0;
    }

    public Table(String name, ArrayList<Field> fields) {
        this.name = name;
        this.fields = new ArrayList<>(fields);
        this.rowCount = 0;
    }

    public Table(JSONObject table) {
        this.fields = new ArrayList<>();

        for (String key: table.keySet()) {
            Field field = new Field(table.getJSONObject(key));
            field.setName((key));
            this.fields.add(field);
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getFieldNames() {
        ArrayList<String> result = this.fields.stream().map(f -> f.getName()).collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    public ArrayList<Field> getFields() {
        return this.fields;
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

    public String getPrimaryFieldName() {
        ArrayList<String> result = this.fields.stream().filter(f -> f.getPrimary()).map(f -> f.getName()).collect(Collectors.toCollection(ArrayList::new));

        if (result.size() == 0) {
            throw new RuntimeException("[" + this.name + "] does not have a primary key!");
        } else if (result.size() > 1) {
            throw new RuntimeException("[" + this.name + "] has multiple primary keys!");
        }

        return result.get(0);
    }

    public ArrayList<Field> getUniqueFields() {
        return this.fields.stream().filter(f -> f.getUnique()).collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean hasOnePrimaryField() {
        ArrayList<String> result = this.fields.stream().filter(f -> f.getPrimary()).map(f -> f.getName()).collect(Collectors.toCollection(ArrayList::new));

        return result.size() == 1;
    }

    public boolean isPrimaryKeyIdentity() {
        if (!hasOnePrimaryField()) {
            return false;
        }

        ArrayList<Field> fields = this.fields.stream().filter(f -> f.getPrimary()).collect(Collectors.toCollection(ArrayList::new));

        return fields.get(0).getIdentity();
    }

    public ArrayList<String> getNullableFields() {
        return this.fields.stream().filter(o -> o.getNullable()).map(o -> o.getName()).collect(Collectors.toCollection(ArrayList::new));
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();

        for (Field field: this.fields) {
            result.put(field.getName(), field.toJSON());
        }

        return result;
    }

}
