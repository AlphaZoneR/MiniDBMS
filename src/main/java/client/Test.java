package client;


import core.Entry;
import core.Table;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        ConnectionManager.sendUseDatabase("World");
//
        Entry e = new Entry("peoples");
        e.add("cnp","261");
        e.add("age", "22");
//
        System.out.println(ConnectionManager.sendInsert("peoples", e));
//      ConnectionManager.sendDeleteAll("embers");

    }
}
