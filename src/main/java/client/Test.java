package client;


import core.Entry;
import core.Table;

public class Test {
        public static void main(String[] args) {
            System.out.println(ConnectionManager.sendCreateDatabase("database3"));
            ConnectionManager.sendUseDatabase("database1");
//            Table users = ConnectionManager.sendGetTable("users");

            Entry entry = new Entry("vines");
            entry.add("name", "Bor3");
            entry.add("country", "HU");
//            System.out.println(entry.toJS);
//            System.out.println(ConnectionManager.sendInsert("vines", entry));
            System.out.println(ConnectionManager.sendDeleteAll("vines"));
            System.out.println(ConnectionManager.sendSelectAll("vines"));


        }
}
