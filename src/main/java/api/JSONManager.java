package api;


import core.Database;
import core.Field;
import core.Table;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JSONManager {
    public static JSONManager manager = new JSONManager();
    private JSONObject structure;

    public JSONManager() {
        try {
            File file = new File(Paths.get("", "data", "structure.json").toAbsolutePath().toString());
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String fileContents = "", st;
            while ((st = bufferedReader.readLine()) != null) {
                fileContents += st;
            }

            bufferedReader.close();

            this.structure = new JSONObject(fileContents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        File file = new File(Paths.get("", "data", "structure.json").toAbsolutePath().toString());

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(this.structure.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDatabase(String name) {
        if (this.isDatabase(name)) {
            return;
        }

        Database database = new Database(name);
        this.structure.getJSONArray("databases").put(database.toJSON());
        this.save();
    }

    public void removeDatabase(String name) {
        if (this.isDatabase(name)) {
            JSONArray databases = this.structure.getJSONArray("databases");
            int index = -1;

            for (int i = 0; i < databases.length(); ++i) {
                if (databases.getJSONObject(i).getString("name").equals(name)) {
                    index = i;
                }
            }

            if (index != -1) {
                this.structure.getJSONArray("databases").remove(index);
            }
        }
        this.save();
    }

    public void addTable(String database, String table) {
        if (this.isDatabase(database)) {
            if (!this.isTable(database, table)) {
                JSONObject databaseObject = this.getDatabase(database);
                Table tableObject = new Table(table);

                databaseObject.getJSONArray("tables").put(tableObject.toJSON());
            }
        }
        this.save();
    }

    public void addTable(String database, Table table) {
        if (this.isDatabase(database) && !this.isTable(database, table.getName())) {
            JSONObject databaseObject = this.getDatabase(database);
            databaseObject.getJSONArray("tables").put(table.toJSON());
        }
        this.save();
    }

    public void addTable(String database, String table, ArrayList<Field> fields) {
        if (this.isDatabase(database) && !this.isTable(database, table)) {
            JSONObject databaseObject = this.getDatabase(database);
            Table tableObject = new Table(table);

            for (Field field : fields) {
                tableObject.addField(field);
            }

            databaseObject.getJSONArray("tables").put(tableObject.toJSON());
        }
        this.save();
    }

    public void removeTable(String database, String table) {
        if (this.isDatabase(database) && this.isTable(database, table)) {
            JSONObject databaseObject = this.getDatabase(database);
            JSONArray tables = databaseObject.getJSONArray("tables");

            int index = -1;

            for (int i = 0; i < tables.length(); ++i) {
                if (tables.getJSONObject(i).getString("name").equals(table)) {
                    index = i;
                }
            }

            if (index != -1) {
                tables.remove(index);
            }
        }
        this.save();
    }

    public JSONObject getTable(String database, String table) {
        if (this.isDatabase(database) && this.isTable(database, table)) {
            JSONArray tables = getDatabase(database).getJSONArray("tables");
            for (int i = 0; i < tables.length(); ++i) {
                if (tables.getJSONObject(i).getString("name").equals(table))  {
                    return tables.getJSONObject(i);
                }
            }
        }

        return null;
    }

    public void editTable(String database, String table, ArrayList<Field> fields) {
        if (this.isDatabase(database) && this.isTable(database, table)) {
            removeTable(database, table);

            Table newTable = new Table(table);

            for (Field field : fields) {
                newTable.addField(field);
            }

            addTable(database, newTable);
        }
        this.save();
    }

    public boolean isDatabase(String name) {
        JSONArray databases = this.structure.getJSONArray("databases");
        for (int i = 0; i < databases.length(); ++i) {
            if (databases.getJSONObject(i).getString("name").equals(name)) {
                return true;
            }
        }

        return false;
    }

    public JSONObject getDatabase(String name) {
        JSONArray databases = this.structure.getJSONArray("databases");

        for (int i = 0; i < databases.length(); ++i) {
            if (databases.getJSONObject(i).getString("name").equals(name)) {
                return databases.getJSONObject(i);
            }
        }

        return null;
    }

    public boolean isTable(String database, String table) {
        if (this.isDatabase(database)) {
            JSONArray tables = this.getDatabase(database).getJSONArray("tables");
            for (int i = 0; i < tables.length(); ++i) {
                if (tables.getJSONObject(i).getString("name").equals(table)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void print() {
        System.out.println(this.structure.toString());
    }

    public JSONObject getDropdown() {
        JSONObject response = new JSONObject();
        JSONArray databases = new JSONArray();

        for (int i = 0; i < this.structure.getJSONArray("databases").length(); ++i) {
            JSONObject database = new JSONObject();
            database.put("name", this.structure.getJSONArray("databases").getJSONObject(i).getString("name"));

            JSONArray tables = new JSONArray();

            for (int j = 0; j < this.structure.getJSONArray("databases").getJSONObject(i).getJSONArray("tables").length(); ++j) {
                tables.put(this.structure.getJSONArray("databases").getJSONObject(i).getJSONArray("tables").getJSONObject(j).getString("name"));
            }

            database.put("tables", tables);

            databases.put(database);
        }

        response.put("databases", databases);

        return response;
    }
}
