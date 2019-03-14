package api;

import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;

public class RedisManager {
    public static RedisManager manager = new RedisManager();
    private Process process;
    private Jedis jedis;

    public RedisManager() {
    }

    public boolean selectDatabase(String name) {
        File file = new File("data/" + name);

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
                this.jedis.disconnect();
            }

            jedis = new Jedis("localhost", 8989);
            jedis.save();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createDatabase(String name) {
        File newDatabaseDirectory = new File("./data/"  + name);
        return newDatabaseDirectory.mkdir();
    }

    public void createTable(String name) {

    }
}
