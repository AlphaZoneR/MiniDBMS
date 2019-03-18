package client;

import core.Database;
import core.Table;

import java.util.ArrayList;

public class Test {
        public static void main(String[] args) {
            ArrayList<Database> foo = ConnectionManager.sendGetDropdown();

            for (Database database : foo) {
                for (Table table: database.getTables()) {
                    System.out.println(table.getName());
                }
            }
        }
}
