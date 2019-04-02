package api;

import core.Entry;
import core.Field;

import java.nio.file.Paths;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
//        try {
//            RedisManager.manager.addDatabase("");
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            RedisManager.manager.addDatabase("Test1");
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            JSONManager.manager.addDatabase("Test1");
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//
//        ArrayList<Field> fields = new ArrayList<>();
//
//        Field field1 = new Field("userid", "int");
//        field1.setPrimary(true);
//        field1.setNull(false);
//        field1.setUnique(true);
//        field1.setIdentity(true);
//
//        Field field2 = new Field("username", "string");
//        field2.setNull(true);
//
//        fields.add(field1);
//        fields.add(field2);
//
//        try {
//            JSONManager.manager.addTable("Test1", "foo", fields);
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//
        try {
            RedisManager.manager.useDatabase("Test1");
            Entry entry = new Entry("foo");
            entry.add("username", "alphazoner");
            GeneralManager.manager.insert(entry);

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

//        try {
//            RedisManager.manager.useDatabase("Test1");
//            GeneralManager.manager.deleteAll("foo");
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            RedisManager.manager.useDatabase("Test1");
//            GeneralManager.manager.deleteWherePrimaryKeyIs("foo", "0");
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }

        try {
            ArrayList<Entry> all = RedisManager.manager.selectAll("foo");

            for (Entry entry: all) {
                System.out.printf(entry.toString());
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }



    }
}