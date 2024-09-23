package com.prediktive.security;

import com.prediktive.domain.TokenValidationResult;
import com.prediktive.domain.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "my-secret-key-human";

    public String generateToken(UserData userData) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("ip", userData.getIp());
        claims.put("userAgent", userData.getUserAgent());
        claims.put("numbers", userData.getNumbers());

        return createToken(claims, userData.getSessionId());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60)) // expire in 60 seconds
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public TokenValidationResult validateToken(String token, UserData userData) {

        if (token == null) {
            return TokenValidationResult.builder().isValid(false).build();
        }
        final String subject = extractSubject(token);
        Claims claims = extractAllClaims(token);
        final String ip = (String) claims.get("ip");
        final String userAgent = (String) claims.get("userAgent");

        String numbersJson = String.valueOf(claims.get("numbers", ArrayList.class));
        Integer sum = Arrays.stream(numbersJson.replaceAll("[\\[\\]\\s]", "").split(","))
                .map(Integer::parseInt)
                .mapToInt(e -> e)
                .sum();

        boolean claimsCheck = subject.equals(userData.getSessionId()) && ip.equals(userData.getIp()) && userAgent.equals(userData.getUserAgent());
        boolean isValid = claimsCheck && !isTokenExpired(token);
        return TokenValidationResult.builder().isValid(isValid).sum(sum).build();
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
