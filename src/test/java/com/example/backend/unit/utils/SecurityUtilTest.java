package com.example.backend.unit.utils;

import com.example.backend.exceptions.FailedCryptionException;
import com.example.backend.utils.SecurityUtil;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import javax.crypto.SecretKey;

@ActiveProfiles("test")
public class SecurityUtilTest {

    private final String TEST_KEY = "zGgUBphf34jh0gUacOo6Qu3knI8bXxYjKfbaDzq9Nts=";
    private final SecretKey SECRET_KEY = SecurityUtil.genKey(TEST_KEY);

    @Test
    void testHashPassword() {
        String str = "password";
        String hashed = SecurityUtil.hashPassword(str);
        assertNotEquals(str, hashed);
        assertTrue(hashed.length() > str.length());
    }

    @Test
    void testCheckPassword() {
        String hashed = SecurityUtil.hashPassword("password");
        assertTrue(SecurityUtil.checkPassword("password", hashed));
        assertFalse(SecurityUtil.checkPassword("Password", hashed));
    }

    @Test
    void testEncrypt() throws FailedCryptionException {
        String str = "secret_text";
        String encryptedStr = SecurityUtil.encrypt(str, SECRET_KEY);
        assertNotEquals(str, encryptedStr);
    }

    @Test
    void testDecrypt() throws FailedCryptionException {
        String str = "secret_text";
        String encryptedStr = SecurityUtil.encrypt(str, SECRET_KEY);
        String decryptedStr = SecurityUtil.decrypt(encryptedStr, SECRET_KEY);
        assertEquals(str, decryptedStr);
    }
}