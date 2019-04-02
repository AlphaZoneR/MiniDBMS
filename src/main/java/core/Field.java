package core;

import org.json.JSONObject;

public class Field {
    private String name;
    private String type;
    private boolean isUnique, isPrimary, isForeign, isIdentity, isNullable;

    public Field(String name, String type) {
        this.name = name;
        this.type = type;
        this.isForeign = false;
        this.isPrimary = false;
        this.isUnique = false;
        this.isIdentity = false;
        this.isNullable = false;
    }

    public Field(JSONObject field) {
        if (!field.has("name") || !field.has("type")) {
            throw new RuntimeException("Invalid Field format!");
        }

        this.name = field.getString("name");
        this.type = field.getString("type");

        this.isForeign = false;
        this.isIdentity = false;
        this.isPrimary = false;
        this.isNullable = false;
        this.isUnique = false;

        if (field.has("isForeign")) {
            this.isForeign = field.getBoolean("isForeign");
        }

        if (field.has("isPrimary")) {
            this.isPrimary = field.getBoolean("isPrimary");
        }

        if (field.has("isUnique")) {
            this.isUnique = field.getBoolean("isUnique");
        }

        if (field.has("isNullable")) {
            this.isNullable = field.getBoolean("isNullable");
        }

        if (field.has("isIdentity")) {
            this.isIdentity = field.getBoolean("isIdentity");
        }
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

    public void setIdentity(boolean isIdentity) {
        this.isIdentity = isIdentity;
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

    public boolean getIdentity() {
        return this.isIdentity;
    }

    public boolean getNullable() {
        return this.isNullable;
    }

    public void setNull(boolean value) {
        this.isNullable = value;
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        result.put("name", this.name);
        result.put("type", this.type);
        result.put("isUnique", this.isUnique);
        result.put("isPrimary", this.isPrimary);
        result.put("isForeign", this.isForeign);
        result.put("isIdentity", this.isIdentity);
        result.put("isNullable", this.isNullable);

        return result;
    }
}
