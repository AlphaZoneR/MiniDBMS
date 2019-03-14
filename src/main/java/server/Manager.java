package server;

import java.util.ArrayList;

public class Manager {
    private ArrayList<String> tables;
    private ArrayList<String> databases;

    public Manager() {
        this.tables = new ArrayList<String>();
        this.databases = new ArrayList<String>();
    }

    public ArrayList<String> getTables() {
        return this.tables;
    }

    public ArrayList<String> getDatabases() {
        return this.databases;
    }
}
