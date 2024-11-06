package client;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import exceptions.DataAccessException;
import model.UserData;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import requests.CreateGameRequest;
import requests.JoinGameRequest;
import responses.*;

public class ServerFacade {
    public class TestServerFacade {
        private final String url;
        private int statusCode;
        private final Gson gson = new Gson();

        public TestServerFacade(String host, String port) {
            url = "http://" + host + ":" + port;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public RegisterResponse register(UserData user) throws ClientException {
            return this.writeToStream("/user", "POST", null, user, RegisterResponse.class);
        }

        public LoginResponse login(UserData user) throws ClientException {
            user = new UserData(user.username(), user.password(), user.email());
            return this.writeToStream("/session", "POST", null, user, LoginResponse.class);
        }

        public LogoutResponse logout(String authToken) throws ClientException {
            return this.writeToStream("/session", "DELETE", authToken, null, LogoutResponse.class);
        }

        public ListGamesResponse listGames(String authToken) throws ClientException {
            return this.writeToStream("/game", "GET", authToken, null, ListGamesResponse.class);
        }

        public CreateGameResponse createGame(CreateGameRequest req, String authToken) throws ClientException {
            return this.writeToStream("/game", "POST", authToken, req, CreateGameResponse.class);
        }

        public JoinGameResponse joinPlayer(JoinGameRequest req, String var2) throws ClientException {
            return this.writeToStream("/game", "PUT", var2, req, JoinGameResponse.class);
        }

        public ClearAllResponse clear() throws ClientException {
            return this.writeToStream("/db", "DELETE", null, null, ClearAllResponse.class);
        }

        public String file(String file) throws ClientException {
            try {
                HttpURLConnection conn;
                (conn = (HttpURLConnection)(new URL(url + file)).openConnection()).setRequestMethod("GET");
                conn.setDoOutput(false);
                conn.connect();
                statusCode = conn.getResponseCode();
                return this.getResponse(conn);
            } catch (IOException var3) {
                throw new ClientException(String.format("Failed to connect to server on GET %s", file));
            }
        }

        private <T> T writeToStream(String path, String method, String authToken, Object req, Class<T> result) throws ClientException {
            try {
                HttpURLConnection conn;
                conn = (HttpURLConnection)(new URL(url + path)).openConnection();
                conn.setRequestMethod(method);
                conn.addRequestProperty("Accept", "application/json");
                if (authToken != null) {
                    conn.addRequestProperty("Authorization", authToken);
                }

                Object var7 = req;
                if (req != null) {
                    conn.setDoOutput(true);
                    OutputStream var10 = conn.getOutputStream();
                    OutputStreamWriter var8;
                    (var8 = new OutputStreamWriter(var10)).write(gson.toJson(var7));
                    var8.flush();
                    var10.close();
                }

                conn.connect();
                return this.getResult(conn, path, method, result);
            } catch (IOException var9) {
                throw new ClientException(String.format("Failed to connect to server on %s %s", method, path));
            }
        }

        private <T> T getResult(HttpURLConnection conn, String path, String method, Class<T> result) throws IOException, ClientException {
            statusCode = conn.getResponseCode();
            if (statusCode != 404 && statusCode != 405) {
                String json;
                if ((json = this.getResponse(conn)).isBlank() || json.equalsIgnoreCase("null")) {
                    json = "{}";
                }

                try {
                    return gson.fromJson(json, result);
                } catch (JsonParseException var5) {
                    throw new ClientException(String.format("%s %s: Error parsing response. Expected JSON, got %s", method, path, json));
                }
            } else {
                throw new ClientException(String.format("Endpoint %s %s not implemented or returned null", method, path));
            }
        }

        private String getResponse(HttpURLConnection conn) throws IOException {
            InputStream iStream;
            if (statusCode / 100 == 2) {
                iStream = conn.getInputStream();
            } else {
                iStream = conn.getErrorStream();
            }

            StringBuilder sb = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(iStream);
            char[] var4 = new char[1024];

            int var5;
            while((var5 = reader.read(var4)) > 0) {
                sb.append(var4, 0, var5);
            }

            iStream.close();
            return sb.toString();
        }
    }
}
