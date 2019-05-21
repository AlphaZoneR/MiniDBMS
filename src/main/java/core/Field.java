package core;

import org.json.JSONObject;

public class Field {
    private String name, ref;
    private int type;
    private boolean isUnique, isPrimary, isIdentity, isNullable;

    public enum Type {
        StringType,
        NumberType,
        DateType,
        ExternalType
        //enum → int
        //yourEnum.ordinal()
        //int → enum
        //EnumType.values()[someInt]
        //String → enum
        //EnumType.valueOf(yourString)
        //enum → String
        //yourEnum.name()
    }

    public Field(String name, int type) {
        this.name = name;
        this.type = type;
        this.ref = "";
        this.isPrimary = false;
        this.isUnique = false;
        this.isIdentity = false;
        this.isNullable = false;
    }

    public Field(String name, String ref) {
        this.name = name;
        this.type = 3;
        this.ref = ref;
        this.isPrimary = false;
        this.isUnique = false;
        this.isIdentity = false;
        this.isNullable = false;
    }

    public Field(JSONObject field) {
        if (!field.has("type")) {
            throw new RuntimeException("Invalid Field format!");
        }

        this.type = field.getInt("type");

        this.isIdentity = false;
        this.isPrimary = false;
        this.isNullable = false;
        this.isUnique = false;

        if (field.has("primary")) {
            this.isPrimary = field.getBoolean("primary");
        }

        if (field.has("unique")) {
            this.isUnique = field.getBoolean("unique");
        }

        if (field.has("null")) {
            this.isNullable = field.getBoolean("null");
        }

        if (field.has("identity")) {
            this.isIdentity = field.getBoolean("identity");
        }

        if (field.has("ref")) {
            this.ref = field.getString("ref");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setUnique(boolean isUnique) {
        this.isUnique = isUnique;

        if (isUnique) {
            this.isNullable = false;
        }
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public void setIdentity(boolean isIdentity) {
        if (!this.isPrimary) {
            return;
        }

        this.isIdentity = isIdentity;

        if (isIdentity) {
            this.isNullable = false;
        }
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public boolean getUnique() {
        return this.isUnique;
    }

    public boolean getPrimary() {
        return this.isPrimary;
    }

    public String getRef() {
        return this.ref;
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
        result.put("type", this.type);
        result.put("unique", this.isUnique);
        result.put("primary", this.isPrimary);
        result.put("ref", this.ref);
        result.put("identity", this.isIdentity);
        result.put("null", this.isNullable);

        return result;
    }
}
