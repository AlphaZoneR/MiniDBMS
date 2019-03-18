package core;

import org.json.JSONObject;

public class Field {
    private String name;
    private String type;
    private boolean isUnique, isPrimary, isForeign;

    public Field(String name, String type) {
        this.name = name;
        this.type = type;
        this.isForeign = false;
        this.isPrimary = false;
        this.isUnique = false;
    }

    public Field(JSONObject field) {
        this.name = field.getString("name");
        this.type = field.getString("type");
        this.isForeign = field.getBoolean("isForeign");
        this.isPrimary = field.getBoolean("isPrimary");
        this.isUnique = field.getBoolean("isUnique");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setForeign(boolean isForeign) {
        this.isForeign = isForeign;
    }

    public void setUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public boolean getUnique() {
        return this.isUnique;
    }

    public boolean getPrimary() {
        return this.isPrimary;
    }

    public boolean getForeign() {
        return this.isForeign;
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        result.put("name", this.name);
        result.put("type", this.type);
        result.put("isUnique", this.isUnique);
        result.put("isPrimary", this.isPrimary);
        result.put("isForeign", this.isForeign);

        return result;
    }
}
