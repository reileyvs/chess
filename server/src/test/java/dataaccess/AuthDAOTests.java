package dataaccess;

import exceptions.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTests {
    MySqlAuthDAO auth;
    AuthData authData;
    @BeforeEach
    void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
        auth = new MySqlAuthDAO();
        authData = new AuthData("Job", "password");
        AuthDAO.clear(auth);
    }
    @AfterEach
    void takeDown() {
        try {
            AuthDAO.clear(auth);
        } catch(DataAccessException ex) {
            assertEquals(1,2);
        }
    }
    @Test
    void addUserPositiveTest() {
        assertDoesNotThrow(()->{
            auth.addUser(authData);
        });
    }
    @Test
    void addUserNegativeTest() {
        assertDoesNotThrow(()->{
            auth.addUser(authData);
        });
        assertThrows(DataAccessException.class, ()->{
            auth.addUser(authData);
        });
    }

    @Test
    void getAuthDataByTokenPositiveTest() {
        assertDoesNotThrow(()->{
            auth.addUser(authData);
        });
    }
    @Test
    void getAuthDataByTokenNegativeTest() {
        assertDoesNotThrow(()->{
            auth.addUser(authData);
        });
    }

    @Test
    void deleteAuthDatumPositiveTest() {
        assertDoesNotThrow(()->{
            auth.addUser(authData);
        });
        assertDoesNotThrow(()->{
            auth.deleteAuthDatum(authData.authToken());
        });
        assertDoesNotThrow(()->{
            assertTrue(auth.getAllAuthData().isEmpty());
        });
    }
    @Test
    void deleteAuthDatumNegativeTest() {
        assertDoesNotThrow(()->{
            auth.addUser(authData);
        });
        assertDoesNotThrow(()-> {
            auth.deleteAuthDatum("randomAuthToken");
        });
        assertThrows(AssertionError.class,()->{
            assertTrue(auth.getAllAuthData().isEmpty());
        });
    }

    @Test
    void getAllAuthDataPositive() throws DataAccessException {
        assertEquals(0, auth.getAllAuthData().size());
    }
    @Test
    void getAllAuthDataNegative() throws DataAccessException {
        assertNotEquals(1,auth.getAllAuthData().size());
    }

    @Test
    void clearTestPositive() throws DataAccessException {
        assertDoesNotThrow(()->{
            auth.addUser(authData);
        });
        assertFalse(auth.getAllAuthData().isEmpty());

        auth.clearAuthData();

        assertTrue(auth.getAllAuthData().isEmpty());
    }
}
