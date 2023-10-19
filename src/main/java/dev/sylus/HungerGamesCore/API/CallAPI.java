package dev.sylus.HungerGamesCore.API;


import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;
import java.util.logging.Level;

public class CallAPI {
    public static void postAPI(String playerName, String playersUUID, String playersName, int playersKills, int playersPoints, int gameNumber, int gameDuration, int timeLeft) {
        try {
            URL url = new URL("https://api.example.com/endpoint"); // This will probbly be api.sylus.dev/someEndpoint

            // Construct the JSON data as a string
            String jsonData = "{\"" + playerName + "\": {" +
                    "\"id\": \"" + playersUUID + "\"," +
                    "\"name\": \"" + playersName + "\"," +
                    "\"kills\": " + playersKills + "," +
                    "\"points\": " + playersPoints + "," +
                    "\"gameNumber\": " + gameNumber + "," +
                    "\"gameDuration\": " + gameDuration + "," +
                    "\"timeLeft\": " + timeLeft +
                    "}}";

            // Create the HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(jsonData);
            osw.flush();
            osw.close();

            // Get the response
            int responseCode = connection.getResponseCode();
            // Handle it here

            connection.disconnect();
        } catch (Exception exception) {
            Bukkit.getLogger().log(Level.SEVERE, exception.toString());
        }
    }

    public void getAPI(){

    }
}
