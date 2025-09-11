package fi.nutrifier.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class RsaKeyConfig {

    @Value("${RSA_PUBLIC_KEY}")
    private String publicKeyBase64;

    @Value("${RSA_PRIVATE_KEY}")
    private String privateKeyBase64;

    @Bean
    public RSAPublicKey publicKey() throws Exception {
        byte[] bytes = Base64.getDecoder().decode(publicKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
    }

    @Bean
    public RSAPrivateKey privateKey() throws Exception {
        byte[] bytes = Base64.getDecoder().decode(privateKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }
}
