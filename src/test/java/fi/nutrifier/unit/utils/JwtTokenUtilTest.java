package fi.nutrifier.unit.utils;

import fi.nutrifier.enums.Role;
import fi.nutrifier.utils.JwtTokenUtil;
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
import java.util.UUID;

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
        String token = jwtTokenUtil.generateToken(UUID.randomUUID(), Role.REGULAR);
        assertNotEquals("123456789", token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    public void testValidateToken_validToken() throws Exception {
        String token = jwtTokenUtil.generateToken(UUID.randomUUID(), Role.REGULAR);
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
        UUID uuid = UUID.randomUUID();
        String token = jwtTokenUtil.generateToken(uuid, Role.REGULAR);
        String extractedId = jwtTokenUtil.extractUserId(token);
        assertEquals(uuid.toString(), extractedId);
    }

    @Test
    public void testExtractRole() throws Exception {
        String token = jwtTokenUtil.generateToken(UUID.randomUUID(), Role.REGULAR);
        List<String> roles = jwtTokenUtil.extractRole(token);
        assertEquals("REGULAR", roles.get(0));

        String token2 = jwtTokenUtil.generateToken(UUID.randomUUID(), Role.PREMIUM);
        List<String> roles2 = jwtTokenUtil.extractRole(token2);
        assertEquals("PREMIUM", roles2.get(0));

        String token3 = jwtTokenUtil.generateToken(UUID.randomUUID(), Role.ADMIN);
        List<String> roles3 = jwtTokenUtil.extractRole(token3);
        assertEquals("ADMIN", roles3.get(0));
    }
}
