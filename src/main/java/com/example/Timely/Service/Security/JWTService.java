package com.example.Timely.Service.Security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    @Value("${app.jwt.secret}")
    private String secreatKey;

    @Value("${app.jwt.expiration}")
    private long expiration;

    public String generateToken(String email) {
        

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {

        byte[] secreatKeyBytes = secreatKey.getBytes();
        
        return Keys.hmacShaKeyFor(secreatKeyBytes);
    }

    public String extractEmail(String token) {

        return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

    }

    public boolean validateToken(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {

        Date expirationDate = Jwts
                                    .parser()
                                    .verifyWith(getKey())
                                    .build()
                                    .parseSignedClaims(token)
                                    .getPayload()
                                    .getExpiration();

        return expirationDate.before(new Date());

    }
    
}
