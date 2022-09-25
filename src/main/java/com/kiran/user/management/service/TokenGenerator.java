package com.kiran.user.management.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenGenerator {

    public String generateJsonWebToken() {
        String secret = "bsdfSES34wfsdgsdfTDSD32dfsddDDerQSNCK36SOWEK5354fdkdf4";

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject("userToken")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(15l, ChronoUnit.MINUTES)))
                .signWith(hmacKey)
                .compact();
    }
}