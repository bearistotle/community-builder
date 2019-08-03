package dev.bearistotle.communitybuilder.models;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class HashUtils {

    private static final int iterations = 65536;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 512;

    public static String getSaltedHash(String password) throws Exception {
        try {
            byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
            return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
        }catch(NoSuchAlgorithmException e){
            // not sure what the correct response to a caught error is here
            return "";
        }
    }
    private static String hash(String password, byte[] salt) throws Exception {
        try {
            if (password == null || password.length() == 0){
                throw new IllegalArgumentException("Passwords cannot be empty.");
            }
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA512");
            SecretKey key = factory.generateSecret(new PBEKeySpec(password.toCharArray(),salt,iterations,desiredKeyLen));

            return Base64.encodeBase64String(key.getEncoded());

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // not sure what to do when error is caught...
            return "";
        }
    }

    public static boolean checkPassword(String passwordEntered, String storedHash) throws Exception{
        String[] saltAndHash = storedHash.split("\\$");
        if (saltAndHash.length != 2) {
            throw new IllegalStateException(
                    "The password stored in the database must have the form 'salt$hash'");
        }
        String hashedInput = hash(passwordEntered, Base64.decodeBase64(saltAndHash[0]));
        return hashedInput.equals(saltAndHash[1]);
    }
}

