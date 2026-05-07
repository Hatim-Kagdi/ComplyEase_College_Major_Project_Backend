package in.complyease.security;


import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import in.complyease.enums.UserRole;

@Component
public class JWTUtil {
    private final String SECRET = "secretkey123secretkey123secretkey123";
    private final javax.crypto.SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String email, UserRole role) {
        return Jwts.builder()
                .setSubject(email) // The "Subject" is the email
                .claim("role", role.name()) // Store enum as String
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

    // Professional way: Get all data from the token
    public io.jsonwebtoken.Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}
