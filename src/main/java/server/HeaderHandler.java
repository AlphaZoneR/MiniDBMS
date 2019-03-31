package server;

import api.JSONManager;
import api.RedisManager;
import core.Field;
import core.Table;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HeaderHandler {

    private interface Command {
        void runCommand(JSONObject header);
    }

    public static HeaderHandler handler = new HeaderHandler();
    private HashMap<String, Command> functions;

    HeaderHandler() {
        this.functions = new HashMap<>();

        this.functions.put("create table", (object) -> {
            if (!object.has("fields") || !object.has("name") || !object.has("database")) {
                throw new RuntimeException("Invalid JSON format!");
            }

            JSONArray fields = object.getJSONArray("fields");
            ArrayList<Field> fieldArrayList = new ArrayList<>();

            for (int i = 0; i < fields.length(); ++i) {
                JSONObject fieldObject = fields.getJSONObject(i);
                if (!fieldObject.has("name") || !fieldObject.has("type")) {
                    throw new RuntimeException("Invalid JSON format!");
                }

                Field field = new Field(fieldObject.getString("name"), fieldObject.getString("type"));

                if (fieldObject.has("isUnique")) {
                    field.setUnique(fieldObject.getBoolean("isUnique"));
                }

                if (fieldObject.has("isForeign")) {
                    field.setForeign(fieldObject.getBoolean("isForeign"));
                }

                if (fieldObject.has("isPrimary")) {
                    field.setPrimary(fieldObject.getBoolean("isPrimary"));

                    if (field.getPrimary()) {
                        field.setUnique(true);
                    }
                }

                fieldArrayList.add(field);
            }

            Table table = new Table(object.getString("name"), fieldArrayList);
            JSONManager.manager.addTable(object.getString("database"), table);
        });

        this.functions.put("drop table", (object) -> {
            if (!object.has("database") || !object.has("name")) {
                throw new RuntimeException("Invalid JSON format!");
            }

            JSONManager.manager.removeTable(object.getString("database"), object.getString("name"));
        });

        this.functions.put("alter table", (object) -> {
            if (!object.has("fields") || !object.has("name") || !object.has("database")) {
                throw new RuntimeException("Invalid JSON format!");
            }

            JSONArray fields = object.getJSONArray("fields");
            ArrayList<Field> fieldArrayList = new ArrayList<>();

            for (int i = 0; i < fields.length(); ++i) {
                JSONObject fieldObject = fields.getJSONObject(i);
                if (!fieldObject.has("name") || !fieldObject.has("type")) {
                    throw new RuntimeException("Invalid JSON format!");
                }

                Field field = new Field(fieldObject.getString("name"), fieldObject.getString("type"));

                if (fieldObject.has("isUnique")) {
                    field.setUnique(fieldObject.getBoolean("isUnique"));
                }

                if (fieldObject.has("isForeign")) {
                    field.setForeign(fieldObject.getBoolean("isForeign"));
                }

                if (fieldObject.has("isPrimary")) {
                    field.setPrimary(fieldObject.getBoolean("isPrimary"));

                    if (field.getPrimary()) {
                        field.setUnique(true);
                    }
                }

                fieldArrayList.add(field);
            }

            JSONManager.manager.editTable(object.getString("database"), object.getString("name"), fieldArrayList);
        });

        this.functions.put("create database", (object) -> {
            if (!object.has("name")) {
                throw new RuntimeException("Invalid JSON format!");
            }

            JSONManager.manager.addDatabase(object.getString("name"));
            RedisManager.manager.addDatabase(object.getString("name"));
        });

        this.functions.put("drop database", (object) -> {
            if (!object.has("name")) {
                throw new RuntimeException("Invalid JSON format!");
            }

            JSONManager.manager.removeDatabase(object.getString("name"));
            RedisManager.manager.removeDatabase(object.getString("name"));
        });

        this.functions.put("use database", (object) -> {
            if(!object.has("name")) {
                throw new RuntimeException("Invalid JSON format!");
            }

             RedisManager.manager.useDatabase(object.getString("name"));
        });
    }

    public void ParsePacket(JSONObject header) {
        if (!header.has("instruction")) {
            throw new RuntimeException("Invalid JSON format!");
        }

        if (this.functions.containsKey(header.getString("instruction").toLowerCase())) {
            this.functions.get(header.getString("instruction").toLowerCase()).runCommand(header);
        }
    }
}
