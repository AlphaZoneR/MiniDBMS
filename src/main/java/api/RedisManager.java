package api;

import core.Table;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import core.Entry;

public class RedisManager {
    public static RedisManager manager = new RedisManager();
    private Process process;
    private Jedis jedis;
    private String currentDatabase;


    public RedisManager() {
    }

    public boolean useDatabase(String name) {
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

    public boolean addDatabase(String name) {
        File newDatabaseDirectory = new File(Paths.get("", "data", name).toAbsolutePath().toString());
        return newDatabaseDirectory.mkdir();
    }

    public boolean removeDatabase(String name) {
        File toDeleteDirectory = new File(Paths.get("", "data", name).toAbsolutePath().toString());

        if (toDeleteDirectory.exists()) {
            return toDeleteDirectory.delete();
        }

        return !toDeleteDirectory.exists();
    }

    public void insert(Entry entry) {
        String table = entry.getTable();
        for (String columnName: entry.getKeys()) {
            jedis.hset(table, columnName, entry.get(columnName));
        }

        this.save();

    }

    public void createTable(String name) {}

    public void save() {
        if (this.jedis != null) {
            this.jedis.save();
        }
    }

    public String getCurrentDatabase() {
        return currentDatabase;
    }
}
