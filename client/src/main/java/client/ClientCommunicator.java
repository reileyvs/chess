package client;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ClientCommunicator {
    static final Gson GSON = new Gson();
    String serverUrl;
    public ClientCommunicator(String url) {
        serverUrl = url;
    }
    public <T> T makeCall(String path, String method, String authToken, Object req, Class<T> result) throws ClientException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.addRequestProperty("Accept", "application/json");
            if (authToken != null) {
                conn.addRequestProperty("Authorization", authToken);
            }

            if (req != null) {
                conn.setDoOutput(true);
                OutputStream stream = conn.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(stream);
                writer.write(GSON.toJson(req));
                writer.flush();
                stream.close();
            }

            conn.connect();
            return doPost(conn, path, method, result);
        } catch (IOException ex) {
            throw new ClientException(String.format("Failed to connect to server on %s %s", method, path));
        } catch (URISyntaxException ex) {
            throw new ClientException("URI failed");
        }
    }

    private <T> T doPost(HttpURLConnection conn, String path, String method, Class<T> result) throws IOException, ClientException {
        int statusCode = conn.getResponseCode();
        if (statusCode != 404 && statusCode != 405) {
            String json = doGet(conn, statusCode);
            if (json.isBlank() || json.equalsIgnoreCase("null")) {
                json = "{}";
            }

            try {
                return GSON.fromJson(json, result);
            } catch (JsonParseException var5) {
                throw new ClientException(String.format("%s %s: Error parsing response. Expected JSON, got %s", method, path, json));
            }
        } else {
            throw new ClientException(String.format("Endpoint %s %s not implemented or returned null", method, path));
        }
    }

    private String doGet(HttpURLConnection conn, int statusCode) throws IOException {
        InputStream iStream;
        if (statusCode / 100 == 2) {
            iStream = conn.getInputStream();
        } else {
            iStream = conn.getErrorStream();
        }

        StringBuilder sb = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(iStream);
        char[] chars = new char[1024];

        int charsToRead;
        while((charsToRead = reader.read(chars)) > 0) {
            sb.append(chars, 0, charsToRead);
        }

        iStream.close();
        return sb.toString();
    }
}
