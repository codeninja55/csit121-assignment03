package application.model.dao;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthenticatorDAO extends DAODecorator {
    private static final String SALT = "#M@rV3!4v3n9eRs";
    private final Path PROGRAM_SETTINGS = Paths.get("com.cn55.marvel/src/persistent_data/settings.txt");
    private boolean adminstratorAccess;

    public AuthenticatorDAO() {

    }

    /*============================== AUTHENTICATION ==============================*/
    public void signup(String username, char[] password) {
        String saltedPassword = SALT + new String(password);

        /*try {
            programSettings.put("username", generatedHashUsername(username));
            programSettings.put("password", generateHashPassword(saltedPassword));
            programSettings.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }*/
    }

    public boolean login(String username, char[] password) {
        String hashedUsername = generatedHashUsername(username);
        String saltedPassword = SALT + new String(password);
        String hashedPassword = generateHashPassword(saltedPassword);

        /*String storedUsername = programSettings.get("username", "");
        String storedPassword = programSettings.get("password", "");*/

        //return storedUsername.equals(hashedUsername) && storedPassword.equals(hashedPassword);
        return false;
    }

    private String generateHashPassword(String saltedPassword) {
        StringBuilder hash = new StringBuilder();
        String generatedPassword = null;

        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = hasher.digest(saltedPassword.getBytes());

            for (byte hashedByte : hashedBytes) {
                hash.append(Integer.toString((hashedByte & 0xff) + 0x100, 16).substring(1));
            }

            generatedPassword = hash.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException: " + e.getCause());
        }
        return generatedPassword;
    }

    private String generatedHashUsername(String username) {
        StringBuilder hash = new StringBuilder();
        String generatedUsername = null;

        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = hasher.digest(username.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };

            for (byte b : hashedBytes) {
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
            generatedUsername = hash.toString();
        } catch (NoSuchAlgorithmException e) { System.out.println("NoSuchAlgorithmException: " + e.getCause()); }
        return generatedUsername;
    }
}
