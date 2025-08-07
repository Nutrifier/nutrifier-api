package com.example.backend.unit.utils;

import com.example.backend.entities.Role;
import com.example.backend.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    private final String privateKeyPEM = System.getenv("RSA_PRIVATE_KEY");
    private final String publicKeyPEM = System.getenv("RSA_PUBLIC_KEY");


    @BeforeEach
    public void setup() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] privateBytes = Base64.getDecoder().decode(privateKeyPEM);
        byte[] publicBytes = Base64.getDecoder().decode(publicKeyPEM);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateBytes);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicBytes);

        RSAPrivateKey mockPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        RSAPublicKey mockPublicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

        // Injecting the keys into the JwtTokenUtil
        jwtTokenUtil = new JwtTokenUtil(mockPrivateKey, mockPublicKey);
    }

    @Test
    public void testGenerateToken() throws Exception {
        String token = jwtTokenUtil.generateToken("test@gmail.com", Role.ROLE_USER);
        assertNotEquals("test@gmail.com", token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    public void testValidateToken_validToken() throws Exception {
        String token = jwtTokenUtil.generateToken("test@gmail.com", Role.ROLE_USER);
        boolean valid = jwtTokenUtil.validateToken(token);
        assertTrue(valid);
    }

    @Test
    public void testValidateToken_invalidToken() throws Exception {
        boolean valid = jwtTokenUtil.validateToken("invalid_token");
        assertFalse(valid);
    }

    @Test
    public void testExtractUser() throws Exception {
        String token = jwtTokenUtil.generateToken("test@gmail.com", Role.ROLE_USER);
        String extractedUsername = jwtTokenUtil.extractUsername(token);
        assertEquals("test@gmail.com", extractedUsername);
    }

    @Test
    public void testExtractRole() throws Exception {
        String token = jwtTokenUtil.generateToken("test@gmail.com", Role.ROLE_USER);
        List<String> roles = jwtTokenUtil.extractRole(token);
        assertEquals("ROLE_USER", roles.get(0));

        String token2 = jwtTokenUtil.generateToken("test@gmail.com", Role.ROLE_ADMIN);
        List<String> roles2 = jwtTokenUtil.extractRole(token2);
        assertEquals("ROLE_ADMIN", roles2.get(0));
    }
}
