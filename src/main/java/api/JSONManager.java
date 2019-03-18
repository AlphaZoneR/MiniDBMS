package api;


import org.json.JSONObject;

import java.io.*;

public class JSONManager {
    public static JSONManager manager = new JSONManager();
    private JSONObject structure;

    public JSONManager() {
        File file = new File("data/structures.json");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String fileContents = "", st;
            while ((st = bufferedReader.readLine()) != null) {
                fileContents += st;
            }

            bufferedReader.close();

            this.structure = new JSONObject(fileContents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        File file = new File("data/structures.json");

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(this.structure.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println(this.structure.toString());
    }
}
