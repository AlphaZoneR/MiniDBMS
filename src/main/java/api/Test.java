package api;

public class Test {
    public static void main(String[] args) {
        RedisManager redisManager = new RedisManager();
        System.out.println(redisManager.createDatabase("temp"));
        redisManager.selectDatabase("temp");

        JSONManager.manager.print();
    }
}