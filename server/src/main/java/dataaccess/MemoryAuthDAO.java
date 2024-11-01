package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryAuthDAO {
    private final ArrayList<AuthData> authData;
    public MemoryAuthDAO() {
        authData = new ArrayList<>();
    }

    public void addUser(AuthData authDatum) {
        deleteAuthDatum(authDatum.authToken());
        authData.add(authDatum);
    }
    public AuthData getAuthDataByUsername(String username) {
        for (AuthData authDatum : authData) {
            if(Objects.equals(authDatum.username(), username)) {
                return authDatum;
            }
        }
        return null;
    }
    public AuthData getAuthDataByToken(String authToken) {
        for (AuthData authDatum : authData) {
            if(Objects.equals(authDatum.authToken(), authToken)) {
                return authDatum;
            }
        }
        return null;
    }
    public void deleteAuthDatum(String authToken) {
        authData.removeIf(authDatum -> Objects.equals(authDatum.authToken(), authToken));
    }
    public void clearAuthData(){
        authData.clear();
    }

    public ArrayList<AuthData> getAllAuthData() {
        return authData;
    }
}
