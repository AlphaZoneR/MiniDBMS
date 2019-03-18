package server;

import api.JSONManager;
import org.json.JSONObject;

import java.net.Socket;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

public class GetHandler {
    public static GetHandler handler = new GetHandler();
    private interface Command {
        JSONObject runCommand(JSONObject header);
    }
    private HashMap<String, Command> functions;

    public GetHandler() {
        this.functions = new HashMap<>();

        this.functions.put("get dropdown", (object) ->
            JSONManager.manager.getDropdown()
        );

        this.functions.put("get table", (object) -> {
            if (!object.has("database") || !object.has("name")) {
                throw new RuntimeException("Invalid JSON format!");
            }

            return JSONManager.manager.getTable(object.getString("database"), object.getString("name"));
        });
    }

    public JSONObject ParsePacket(JSONObject header) {
        if (!header.has("instruction")) {
            throw new RuntimeException("Invalid JSON format!");
        }

        System.out.println("[" + new Date() + "]" + " " + header.getString("instruction"));

        if (!header.getString("instruction").toLowerCase().contains("get")) {
            return new JSONObject("{\"message\": 200}");
        }

        if (!this.functions.containsKey(header.getString("instruction").toLowerCase())) {
            return new JSONObject("{\"message\": 503}");
        }

        return this.functions.get(header.getString("instruction").toLowerCase()).runCommand(header);
    }
}
