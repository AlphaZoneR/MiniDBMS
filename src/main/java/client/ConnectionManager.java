package client;

import core.Database;
import core.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionManager {
    private static String send(JSONObject object) {
        try {
            Socket socket = new Socket("localhost", 8990);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.write(object.toString());
            out.flush();

            char[] response = new char[2048];

            int read = in.read(response);

            in.close();
            out.close();
            socket.close();

            return String.valueOf(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void sendCreateDatabase(String name) {
        JSONObject result = new JSONObject();
        result.put("instruction", "create database");
        result.put("name", name);

        send(result);
    }

    public static void sendCreateDatabase(Database database) {
        JSONObject result = new JSONObject();
        sendCreateDatabase(database.getName());

        for (Table table: database.getTables()) {
            sendCreateTable(database.getName(), table);
        }
    }

    public static void sendDropDatabase(String name) {
        JSONObject result = new JSONObject();
        result.put("instruction", "drop database");
        result.put("name", name);

        send(result);
    }

    public static void sendCreateTable(String database, Table table) {
        JSONObject result = table.toJSON();
        result.put("database", database);
        result.put("instruction", "create table");

        send(result);
    }

    public static void sendDropTable(String database, String table) {
        JSONObject result = new JSONObject();
        result.put("name", table);
        result.put("database", database);
        result.put("instruction", "drop table");

        send(result);
    }

    public static void sendDropTable(String database, Table table) {
        JSONObject result = new JSONObject();
        result.put("name", table.getName());
        result.put("database", database);
        result.put("instruction", "drop table");

        send(result);
    }

    public static ArrayList<Database> sendGetDropdown() {
        JSONObject request = new JSONObject();
        request.put("instruction", "get dropdown");

        try {
            String response = send(request);
            ArrayList<Database> result = new ArrayList<>();
            if (response != null) {
                JSONObject data = new JSONObject(response);
                JSONArray databases = data.getJSONArray("databases");
                for (int i = 0; i < databases.length(); ++i) {
                    Database database = new Database(databases.getJSONObject(i).getString("name"));

                    JSONArray tables = databases.getJSONObject(i).getJSONArray("tables");

                    for (int j = 0; j < tables.length(); ++j) {
                        Table table = new Table(tables.getString(j));
                        database.addTable(table);
                    }
                    result.add(database);
                }

                return result;
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        return null;
    }

    public Table sendGetTable(String database, String name) {
        JSONObject request = new JSONObject();
        request.put("database", database);
        request.put("name", name);

        try {
            String resultString = send(request);

            if (resultString != null) {
                JSONObject object = new JSONObject(resultString);
                return new Table(object);
            }

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        return null;
    }

}
