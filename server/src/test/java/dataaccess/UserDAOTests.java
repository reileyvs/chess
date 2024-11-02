package dataaccess;

import exceptions.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDAOTests {
    MySqlUserDAO use;
    UserData user;
    @BeforeEach
    void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
        use = new MySqlUserDAO();
        user = new UserData("Job", "password", "bla@.og");
    }
    @AfterEach
    void takeDown() {
        try {
            UserDAO.clear(use);
        } catch(DataAccessException ex) {
            assertEquals(1,2);
        }
    }
    @Test
    void addUserPositiveTest() {
        assertDoesNotThrow(()-> {
            use.addUser(user);
        });
    }
    @Test
    void addUserNegativeTest() {
        assertDoesNotThrow(()-> {
            use.addUser(user);
        });
        assertThrows(DataAccessException.class, ()-> {
           use.addUser(new UserData("Job","notPassword","ble@.og"));
        });
    }

    @Test
    void getUserPostiveTest() {
        assertDoesNotThrow(()-> {
            use.addUser(user);
        });
        assertDoesNotThrow(()->{
            UserData foundUser = use.getUser(user.username());
            assertEquals(user.email(),foundUser.email());
        });
    }
    @Test
    void getUserNegativeTest() {
        assertDoesNotThrow(()->{
            assertNull(use.getUser("baaa"));
        });
    }

    @Test
    void clearTestPositive() throws DataAccessException {
        use.addUser(new UserData("bob","pass","@email.com"));
        assertFalse(use.getUsers().isEmpty());

        use.clearUsers();

        assertTrue(use.getUsers().isEmpty());
    }
}
