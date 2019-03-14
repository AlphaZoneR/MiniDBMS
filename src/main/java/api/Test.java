package api;

import java.util.concurrent.TimeoutException;

public class Test {
    public static void main(String[] args) {
        RedisManager redisManager = new RedisManager();
        System.out.println(redisManager.createDatabase("temp"));
        redisManager.selectDatabase("temp");
    }
}
