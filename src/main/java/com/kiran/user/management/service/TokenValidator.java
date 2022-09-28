package com.kiran.user.management.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
public class TokenValidator {

    @Value("${server.secret.key}")
    private String secret;

    /**
     * Validates token
     * @param jwtToken token to be validated
     * @return true if successful otherwise false
     */
    public boolean validate(String jwtToken) {
        try {
            if (!validateToken(jwtToken)) {
                return false;
            }
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    /**
     * Business logic to validate token
     * @param jwtToken token to be validated
     * @return true if successful otherwise false
     */
    public boolean validateToken(String jwtToken) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());
        try {
            Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(jwtToken);
        } catch (RuntimeException e) {
            return false;
        }

        return true;
    }

}
