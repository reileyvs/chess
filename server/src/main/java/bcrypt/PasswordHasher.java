package bcrypt;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public boolean checkHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}