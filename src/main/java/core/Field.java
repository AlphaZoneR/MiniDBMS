package core;

public class Field {
    private String name;
    private String type;


    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public Field(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
