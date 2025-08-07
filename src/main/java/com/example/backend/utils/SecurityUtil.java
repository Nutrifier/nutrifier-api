package com.example.backend.utils;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import com.example.backend.entities.User;
import com.example.backend.exceptions.EncryptionKeyException;
import com.example.backend.exceptions.FailedCryptionException;
import com.example.backend.exceptions.FailedDecryptionException;
import com.example.backend.exceptions.FailedEncryptionException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import java.nio.charset.Charset;
//import javax.crypto.KeyGenerator;

public class SecurityUtil {

    private final static Argon2PasswordEncoder e = new Argon2PasswordEncoder(
        16, 32, 1, 60000, 10
    );

    public static String hashPassword(String password) {
        return e.encode(password);
    }

    public static boolean checkPassword(String plain, String hashed) {
        return e.matches(plain, hashed);
    }

    public static String encrypt(String str) throws FailedCryptionException, EncryptionKeyException {
        return processCipher(str, getKey(), Cipher.ENCRYPT_MODE);
    }

    public static String encrypt(String str, SecretKey key) throws FailedCryptionException {
        return processCipher(str, key, Cipher.ENCRYPT_MODE);
    }

    public static String decrypt(String str) throws FailedCryptionException, EncryptionKeyException {
        return processCipher(str, getKey(), Cipher.DECRYPT_MODE);
    }

    public static String decrypt(String str, SecretKey key) throws FailedCryptionException {
        return processCipher(str, key, Cipher.DECRYPT_MODE);
    }

    private static String processCipher(String str, SecretKey key, int mode) throws FailedCryptionException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, key);

            if (mode == Cipher.ENCRYPT_MODE) {
                byte[] bytes = str.getBytes(Charset.defaultCharset());
                byte[] encryptedBytes = cipher.doFinal(bytes);
                return Base64.getEncoder().encodeToString(encryptedBytes);
            } else {
                byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(str));
                return new String(decryptedBytes);
            }

        } catch (Exception e) {
            if (mode == Cipher.ENCRYPT_MODE) {
                throw new FailedEncryptionException(e.getMessage());
            } else {
                throw new FailedDecryptionException(e.getMessage());
            }
        }
    }

    public static SecretKeySpec genKey(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }

    private static SecretKey getKey() throws EncryptionKeyException {
        String base64Key = System.getenv("AES_KEY");

        // Throw error if key is not found in environmental variables
        if (base64Key == null) throw new EncryptionKeyException();

        return genKey(base64Key);
    }
}
