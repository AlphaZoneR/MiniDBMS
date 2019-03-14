package api;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class JSONManager {
    private static JSONManager manager = new JSONManager();
    private JSONObject structureJSON;

    public JSONManager() {
        try {
            InputStream is = new FileInputStream("data/structure.json");
            this.structureJSON = new JSONObject(IOUtils.toString(is, "utf-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public JSONObject getStructureJSON() {
        return this.structureJSON;
    }

    public void addDatabase(String database) {
        JSONArray databases = this.structureJSON.getJSONArray("databases");
        JSONObject newDatabase = new JSONObject();

        newDatabase.put("name", database);
        newDatabase.put("tables", new JSONArray());

        databases.put(newDatabase);
    }

    public void addTable(String database, String table, String[] keys, String[] types) {
        JSONArray databases = this.structureJSON.getJSONArray("databases");

        for (int i = 0; i < databases.length(); ++i) {
            JSONObject db = databases.getJSONObject(i);

            if (db.getString("name").equals(database)) {

                break;
            }
        }
    }

    public static JSONObject getStructure() {
        return manager.getStructureJSON();
    }
}
