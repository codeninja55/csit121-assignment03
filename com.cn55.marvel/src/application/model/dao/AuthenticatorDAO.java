package application.model.dao;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AuthenticatorDAO {
    private static final String SALT = "#M@rV3!4v3n9eRs";
    private final Path PROGRAM_SETTINGS = Paths.get("com.cn55.marvel/src/persistent_data/users_credentials");
    private final Path ADMIN_SETTINGS = Paths.get("com.cn55.marvel/src/persistent_data/administrator_credentials");
    private final String[] administrator;
    private final HashMap<String, String> users;

    public AuthenticatorDAO() {
        this.administrator = new String[2];
        this.users = new LinkedHashMap<>();
    }

    /*============================== IMPORT USERS ==============================*/
    public void importUsers() {
        SwingWorker<Void, Boolean> importWorker = new SwingWorker<Void, Boolean>() {
            protected Void doInBackground() throws Exception {
                Files.lines(PROGRAM_SETTINGS).forEach(line -> {
                    String[] lineArr = line.split(":");
                    users.put(lineArr[0], lineArr[1]);
                });

                List<String> fileLine = Files.readAllLines(ADMIN_SETTINGS);
                administrator[0] = fileLine.get(0).substring(0, fileLine.get(0).indexOf(":"));
                administrator[1] = fileLine.get(0).substring(fileLine.get(0).indexOf(":") + 1);

                return null;
            }
        };
        importWorker.execute();
    }

    public void exportUsers() {
        SwingWorker<Void, Void> exportWorker = new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                BufferedWriter writer = Files.newBufferedWriter(PROGRAM_SETTINGS, Charset.defaultCharset(),
                                                                StandardOpenOption.CREATE,
                                                                StandardOpenOption.WRITE);

                users.entrySet().forEach((entry) -> {
                    try {
                        writer.append(entry.getKey()).append(":").append(entry.getValue());
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                writer.flush();
                writer.close();

                String administratorCredentials = administrator[0] + ":" + administrator[1];
                Files.write(ADMIN_SETTINGS, administratorCredentials.getBytes());

                return null;
            }
        };

        exportWorker.execute();
    }

    /*============================== AUTHENTICATION ==============================*/

    public void signup(String username, char[] password) {
        String saltedPassword = SALT + new String(password);
        users.put(generatedHashUsername(username), generateHashPassword(saltedPassword));
    }

    public boolean login(String username, char[] password) {
        String hashedUsername = generatedHashUsername(username);
        String saltedPassword = SALT + new String(password);
        String hashedPassword = generateHashPassword(saltedPassword);

        return administrator[0].equals(hashedUsername) && administrator[1].equals(hashedPassword) ||
                users.containsKey(hashedUsername) && users.containsValue(hashedPassword);
    }

    /*
    * Title: Java SHA Hashing Example
    * Author: mkyong
    * Date: 24-02-2010
    * Availability: https://www.mkyong.com/java/java-sha-hashing-example/
    * */

    /*
    * Title: Generate Secure Password Hash : MD5, SHA, PBKDF2, BCrypt Examples
    * Author: Lokesh Gupta
    * Date: 22-07-2013
    * Availability: https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    * */

    /*
    * Title: SHA-256 Hashing in Java
    * Author: baeldung
    * Date: 20-11-2016
    * Availability: http://www.baeldung.com/sha-256-hashing-java
    * */

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
