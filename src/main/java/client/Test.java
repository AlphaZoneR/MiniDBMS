package client;


import core.Entry;
import core.Table;

import java.util.ArrayList;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        ConnectionManager.sendUseDatabase("database2");
        ArrayList<Entry> results = ConnectionManager.sendQuery("select * from users join cities on users.city=cities.cityid where users.country~=R*");
        for (Entry result: results) {
            System.out.println(result);
        }
        //      ConnectionManager.sendDeleteAll("embers");

    }
}
