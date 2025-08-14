package fi.nutrifier.utils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import fi.nutrifier.entities.Role;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.crypto.RSASSASigner;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtTokenUtil(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String generateToken(String username, Role role) throws JOSEException {
        System.out.println("Generating token with role: " + role.toString());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("roles", List.of(role.toString()))
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .build();

        RSASSASigner signer = new RSASSASigner(privateKey);
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            RSASSAVerifier verifier = new RSASSAVerifier(publicKey);
            if (signedJWT.verify(verifier)) {
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                return claims.getExpirationTime().after(new Date());
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public String extractUsername(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    public List<String> extractRole(String token) throws ParseException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return (List<String>) signedJWT.getJWTClaimsSet().getClaim("roles");
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
