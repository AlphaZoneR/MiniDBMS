package server;

import api.JSONManager;
import api.RedisManager;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketOption;
import java.net.SocketTimeoutException;
import java.util.Date;

public class Server {
    private int PORT = 8990;

    private ServerSocket serverSocket;

    public Server() {
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        // Note: make it thread-safe;
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                    System.out.println("[" + new Date() + "]" + " Performing scheduled backup save!");
                    JSONManager.manager.save();
                    RedisManager.manager.save();
                } catch (InterruptedException interruptedException) {
                    System.out.println("[" + new Date() + "]" + " " + interruptedException);
                }
            }
        });

        while (true) {
            try {
                Socket s = serverSocket.accept();
                s.setSoTimeout(5000);

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    PrintWriter out = new PrintWriter(s.getOutputStream(), true);

                    char[] input = new char[2048];
                    int read = in.read(input);

                    if (read == 0) {
                        throw new RuntimeException("Invalid data amount sent!");
                    }

                    String inputString = String.valueOf(input);
                    JSONObject object = new JSONObject(inputString);

                    HeaderHandler.handler.ParsePacket(object);
                    JSONObject response = GetHandler.handler.ParsePacket(object);

                    out.write(response.toString());
                    out.flush();
                } catch (SocketTimeoutException socketTimeoutException) {
                    System.out.println("[" + new Date() + "]" + " " + socketTimeoutException);
                } catch (JSONException jsonException) {
                    System.out.println("[" + new Date() + "]" + " " + jsonException);
                } catch (RuntimeException runtimeException) {
                    System.out.println("[" + new Date() + "]" + " " + runtimeException);
                } finally {
                    s.close();
                }

            } catch (IOException e) {
                System.out.println("[" + new Date() + "]" + " " + e);
//                e.printStackTrace();
            }
        }
    }
}
