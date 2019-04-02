package api;

import core.Field;
import core.Table;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

import core.Entry;

public class RedisManager {
    public static RedisManager manager = new RedisManager();
    private Process process;
    private Jedis jedis;
    private String currentDatabase;


    public RedisManager() {
    }

    public boolean useDatabase(String name) {
        if (name == "") {
            throw new RuntimeException(String.format("'' is not a valid database name!"));
        }

        File file = new File(Paths.get("", "data", name).toAbsolutePath().toString());

        if (!file.exists()) {
            System.out.println("Error: Cannot select database [" + name + "]. Does not exist!");
            return false;
        }

        try {
            if (this.process != null) {
                this.process.destroy();
            }
            this.process = Runtime.getRuntime().exec("redis-server --port 8989 --dir ./data/" + name);

            if (this.jedis != null) {
                this.jedis.save();
                this.jedis.disconnect();
            }

            this.currentDatabase = name;

            jedis = new Jedis("localhost", 8989);
            jedis.save();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void addDatabase(String name) {
        if (name == "") {
            throw new RuntimeException(String.format("'' is not a valid database name!"));
        }

        File newDatabaseDirectory = new File(Paths.get("", "data", name).toAbsolutePath().toString());
        if (!newDatabaseDirectory.mkdir()) {
            throw new RuntimeException(String.format(String.format("Cannot create database [%s] (probably because it already exists)", name)));
        }
    }

    public boolean removeDatabase(String name) {
        if (name == "") {
            throw new RuntimeException(String.format("'' is not a valid database name!"));
        }

        File toDeleteDirectory = new File(Paths.get("", "data", name).toAbsolutePath().toString());

        if (toDeleteDirectory.exists()) {
            return toDeleteDirectory.delete();
        }

        return !toDeleteDirectory.exists();
    }

    public void insertIndex(String table, String uniqueField, String uniqueFieldValue, String primaryKeyValue) {
        this.jedis.hset(table, String.format("%s#%s", uniqueField, uniqueFieldValue), primaryKeyValue);
    }

    public String getIndex(String table, String uniqueField, String uniqueFieldValue) {
        return this.jedis.hget(table, String.format("%s#%s", uniqueField, uniqueFieldValue));
    }

    public void insert(Entry entry) {
        String table = entry.getTable();
        Table tableObject = new Table(JSONManager.manager.getTable(this.currentDatabase, table));
        String primaryKeyName = tableObject.getPrimaryFieldName();
        String entryPrimaryKeyValue = entry.get(primaryKeyName);

        if (this.jedis.hget(table, entryPrimaryKeyValue) != null) {
            throw new RuntimeException(String.format("Cannot insert into table [%s] entry with primary key [%s] because it already exists!", table, entryPrimaryKeyValue));
        }

        for (Field field: tableObject.getFields()) {
            if (!field.getNullable() && !entry.has(field.getName())) {
                throw new RuntimeException(String.format("Cannot insert into table [%s] entry with primary key [%s] because not nullable field [%s] is missing!", table, entryPrimaryKeyValue, field.getName()));
            }

            if (field.getUnique()) {
                if (getIndex(table, field.getName(), entry.get(field.getName())) != null) {
                    throw new RuntimeException(String.format("Cannot insert into table [%s] entry with primary key [%s] because unique field [%s] with value [%s] breaks unique constraint!", table, entryPrimaryKeyValue, field.getName(), entry.get(field.getName())));
                }
            }
        }

        ArrayList<String> entryValues = new ArrayList<>();

        JSONObject object = new JSONObject();

        for (Field field: tableObject.getFields()) {
            if (field.getNullable() && !entry.has(field.getName())) {
                object.put(field.getName(), "null");
            } else if (entry.has(field.getName())) {
                entryValues.add(entry.get(field.getName()));
                object.put(field.getName(), entry.get(field.getName()));
            }

            if (field.getUnique() && !field.getPrimary()) {
                this.insertIndex(table, field.getName(), entry.get(field.getName()), entryPrimaryKeyValue);
            }
        }

        this.jedis.hset(table, entryPrimaryKeyValue, object.toString());
        this.save();

    }

    public ArrayList<Entry> selectAll(String table) {
        if (!JSONManager.manager.isTable(currentDatabase, table)) {
            throw new RuntimeException(String.format("Cannot select all from table [%s] because it does not exist!", table));
        } else {
            Map<String, String> everything = jedis.hgetAll(table);
            Table tableObject = new Table(JSONManager.manager.getTable(currentDatabase, table));



            if (everything != null) {
                ArrayList<Entry> result = new ArrayList<>();
                for (String primaryKey: everything.keySet()) {
                    Entry entry = new Entry(table);
                    String jsonString = everything.get(primaryKey);

                    if (primaryKey.contains("#")) {
                        continue;
                    }

                    try {
                        JSONObject keyValueJson = new JSONObject(jsonString);

                        keyValueJson.put(tableObject.getPrimaryFieldName(), primaryKey);

                        for (Field field: tableObject.getFields()) {
                            if (!keyValueJson.has(field.getName())) {
                                throw new RuntimeException(String.format("Cannot retrieve object with primary key [%s] from table [%s] because the format is malformed!", primaryKey, table));
                            } else {
                                entry.add(field.getName(), keyValueJson.getString(field.getName()));
                            }
                        }

                        result.add(entry);

                    } catch (JSONException e) {
                        throw new RuntimeException(String.format("Cannot retrieve object with primary key [%s] from table [%s] because: [%s]", primaryKey, table, e.toString()));
                    }
                }

                return result;
            } else {
                throw new RuntimeException(String.format("Cannot select all from table [%s] because of an unexpected error!", table));
            }
        }
    }

    public void createTable(String name) {

    }

    public void deleteAll(String table) {
        if (!JSONManager.manager.isTable(currentDatabase, table)) {
            throw new RuntimeException(String.format("Cannot delete all from table [%s] because it does not exist!", table));
        }

        Map<String, String> all = this.jedis.hgetAll(table);

        for (String key: all.keySet()) {
            this.jedis.hdel(table, key);
        }

        this.save();
    }


    public void deleteWherePrimaryKeyIs(String table, String value) {
        if (!JSONManager.manager.isTable(currentDatabase, table)) {
            throw new RuntimeException(String.format("Cannot delete entry with primary key [%s] from table [%s] because table does not exist!", value, table));
        }

        Table tableObject = new Table(JSONManager.manager.getTable(currentDatabase, table));

        String entryJsonString = this.jedis.hget(table, value);

        if (entryJsonString != null) {
            JSONObject jsonObject = new JSONObject(entryJsonString);

            for (Field field: tableObject.getUniqueFields()) {
                if (field.getUnique() && !field.getPrimary()) {
                    String uniqueKeyValue = jsonObject.getString(field.getName());
                    this.jedis.hdel(table, String.format("%s#%s", field.getName(), uniqueKeyValue));
                }
            }
            this.jedis.hdel(table, value);
            this.save();
        } else {
            throw new RuntimeException(String.format("Cannot delete entry with primary key [%s] from table [%s] because it does not exist!", value, table));
        }

    }

    public void save() {
        if (this.jedis != null) {
            this.jedis.save();
        }
    }

    public String getCurrentDatabase() {
        return currentDatabase;
    }
}
