package org.example.techstore.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.techstore.entity.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${application.jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${application.jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;
    @Value("${application.jwt.expiration}")
    private Long accessExpiration;

    // --- ACCESS TOKEN (15 phút) ---
    public String generateToken(Account accountEntity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", "ACCESS");
        claims.put("accountId", accountEntity.getId());
        claims.put("roleId", accountEntity.getRole().getId());
        claims.put("role", accountEntity.getRole().getRoleCode());
        return buildToken(claims, accountEntity.getUsername(), accessExpiration);
    }

    public String generateRefreshToken(Account accountEntity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", "REFRESH");
        return buildToken(claims, accountEntity.getUsername(), refreshTokenExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    public Long getAccountIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("accountId", Long.class));
    }

    public Long getRestaurantIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("restaurantId", Long.class));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    //Hành động verify token đã được diễn ra khi thực hiện Claim JWT thông qua .parseClaimsJws(token)
    public boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }


    public boolean isRefreshTokenValid(String token, String username) {
        try {
            final String tokenUsername = extractUsername(token);
            return tokenUsername.equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isAccessToken(String token) {
        try {
            String type = extractClaim(token, claims -> claims.get("token_type", String.class));
            return "ACCESS".equals(type);
        } catch (Exception e) {
            return false;
        }
    }
}
