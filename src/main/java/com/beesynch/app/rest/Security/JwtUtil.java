package com.beesynch.app.rest.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private static final String SECRET_KEY = "may_we_pass_this_term_with_flying_grades";
    private static final long EXPIRATION_TIME = 86400000; // 24 hours (in milliseconds)

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Modify the generateToken method to use user ID
    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract user ID from token
    public Long extractUserId(String token) {
        String userId = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.parseLong(userId);
    }

    // Validate token without UserDetails
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Validate token with UserDetails
    public boolean isValidToken(String token, UserDetails userDetails) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token invalid
            return false;
        }
    }

    // Generate a password reset token (with shorter expiration)
    public String generatePasswordResetToken(Long userId) {
        Date now = new Date();
        // Set expiration to 15 minutes for security
        Date expiryDate = new Date(now.getTime() + 15 * 60 * 1000); // 15 minutes

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .claim("type", "password_reset") // Add a claim to identify this as a password reset token
                .compact();
    }

    // Extract user ID from reset token
    public Long extractUserIdFromResetToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Verify this is a password reset token
            String tokenType = claims.get("type", String.class);
            if (!"password_reset".equals(tokenType)) {
                return null;
            }

            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            return null; // Token is invalid or expired
        }
    }

}