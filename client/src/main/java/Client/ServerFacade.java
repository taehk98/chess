package Client;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.io.*;
import java.net.*;

public class ServerFacade {
    String serverUrl = null;
   HttpURLConnection http = null;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData addUser(UserData user) throws DataAccessException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public AuthData getUser(UserData user) throws DataAccessException {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            http = (HttpURLConnection) url.openConnection();

            http.setReadTimeout(5000);
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();


            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new DataAccessException("Request Error");
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            try (OutputStream reqBody = http.getOutputStream();) {
                String reqData = new Gson().toJson(request);
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new DataAccessException("failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
