package client;

import core.Database;
import core.Entry;
import core.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ConnectionManager {
    private static String readFromInputStream(InputStream is) {
        StringBuilder stringBuilder = new StringBuilder();

        try (Reader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))) {
            int c = 0;

            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return stringBuilder.toString();
    }

    private static String send(String url, String method) {
        try {
            URL mUrl = new URL(url);
            URLConnection urlConnection = mUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoOutput(true);

            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                return readFromInputStream(httpURLConnection.getInputStream());
            }

            return readFromInputStream(httpURLConnection.getErrorStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return e.toString();
        } catch (IOException e1) {
            e1.printStackTrace();
            return e1.toString();
        }
    }

    private static String sendWithBody(String url, String method, JSONObject body) {
        try {
            URL mUrl = new URL(url);
            URLConnection urlConnection = mUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

            httpURLConnection.setRequestMethod(method);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);

            httpURLConnection.connect();

            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = body.toString().getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            if (httpURLConnection.getResponseCode() == 200) {
                return readFromInputStream(httpURLConnection.getInputStream());
            }

            return readFromInputStream(httpURLConnection.getErrorStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return e.toString();
        } catch (IOException e1) {
            e1.printStackTrace();
            return e1.toString();
        }
    }


    public static String sendCreateDatabase(String name) {
        return send("http://localhost:5000/database/" + name, "POST");
    }


    public static String sendDropDatabase(String name) {
        return send("http://localhost:5000/database/" + name, "DELETE");
    }

    public static String sendUseDatabase(String name) {
        return send("http://localhost:5000/database/use/" + name, "GET");
    }

    public static String sendCreateTable(Table table) {
        return sendWithBody("http://localhost:5000/tmanage/" + table.getName(), "POST", table.toJSON());
    }

    public static String sendDropTable(String name) {
        return send("http://localhost:5000/tmanage/" + name, "DELETE");
    }

    public static ArrayList<Database> sendGetDropdown() {
        String databasesString  = send("http://localhost:5000/database", "GET");

        try {
            JSONObject databases = new JSONObject((databasesString));
            ArrayList<Database> result = new ArrayList<>();

            for (String databaseKey: databases.keySet()) {
                Database database = new Database(databaseKey);

                for (String tableKey: databases.getJSONObject(databaseKey).keySet()) {
                    Table table = new Table(databases.getJSONObject(databaseKey).getJSONObject(tableKey));
                    table.setName(tableKey);
                    database.addTable(table);
                }

                result.add(database);
            }

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Table sendGetTable(String name) {
        String result = send("http://localhost:5000/tmanage/" + name, "GET");
        try {
            JSONObject table = new JSONObject(result);
            Table tObject = new Table(table);
            tObject.setName(name);

            return tObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Entry> sendSelectAll(String name) {
        String result = send("http://localhost:5000/table/" + name, "GET");
        ArrayList<Entry> entries = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); ++i) {
                Entry entry = new Entry(array.getJSONObject(i));
                entry.setTable(name);
                entries.add(entry);
            }

            return entries;
        } catch (JSONException e) {
            return new ArrayList<>();
        }
    }

    public static Entry sendSelectByPrimary(String name, String primary) {
        String result = send("http://localhost:5000/table/" + name + "/" + primary, "GET");
        try {
            JSONObject jsonEntry = new JSONObject(result);
            Entry entry = new Entry(jsonEntry);
            entry.setTable(name);

            return entry;
        } catch (JSONException e) {
            return null;
        }
    }

    public static ArrayList<Entry> sendSelectByFilter(String name, JSONObject filter) {
        String result = sendWithBody("http://localhost:5000/table/filter/" + name, "POST", filter);

        ArrayList<Entry> entries = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); ++i) {
                Entry entry = new Entry(array.getJSONObject(i));
                entry.setTable(name);
                entries.add(entry);
            }

            return entries;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static String sendInsert(String name, Entry entry) {
        return sendWithBody("http://localhost:5000/table/" + name, "POST", entry.toJSON());
    }

    public static String sendInsert(String name, String primary, Entry entry) {
        return sendWithBody("http://localhost:5000/table/" + name + "/" + primary, "POST", entry.toJSON());
    }

    public static String sendDeleteAll(String name) {
        return sendWithBody("http://localhost:5000/table/" + name, "DELETE", new JSONObject());
    }

    public static String sendDeleteByPrimary(String name, String primary) {
        return send("http://localhost:5000/table/" + name + "/" + primary, "DELETE");
    }

    public static String sendDeleteByFilter(String name, JSONObject filter) {
        return sendWithBody("http://localhost:5000/table/" + name, "DELETE", filter);
    }



}
