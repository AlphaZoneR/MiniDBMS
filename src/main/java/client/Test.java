package client;
import core.Database;
import core.Table;

public class Test {
        public static void main(String[] args) {
//            ConnectionManager.sendCreateDatabase("beigli");
            ConnectionManager.sendUseDatabase("beigli");
        }
}
