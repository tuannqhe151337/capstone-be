package com.example.capstone_project.utils.helper;


import com.example.capstone_project.entity.AccessTokenClaim;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {
//    private final String SECRET_KEY = "a8dbfdAdohfod339fasdalDhdhoiudhoauho3sdufDUauhdiduuad28824";
//    private final long EXPIRATION = 1000 * 60 * 60 * 24; // milliseconds for 1 day
//    private final String SECRET_REFRESH_TOKEN_KEY = "asdj3usodfj39dfHDfdoh3au3dhdUbdHDF3douia9472djhaodu";
//    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 2; // milliseconds for 2 days

    @Value("${application.security.access-token.secret-key}")
    private String ACCESS_TOKEN_SECRET_KEY;

    @Value("${application.security.access-token.expiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${application.security.refresh-token.secret-key}")
    private String SECRET_REFRESH_TOKEN_KEY;

    @Value("${application.security.refresh-token.expiration}")
    private long REFRESH_TOKEN_EXPIRATION;

    public String generateRefreshToken(Integer userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(this.getRefreshTokenSecretKey())
                .compact();
    }

    public String generateAccessToken(Integer userId, String roleCole, long departmentId) {
        HashMap<String, Object> claims =  new HashMap<>();
        claims.put("role", roleCole);
        claims.put("departmentId", departmentId);

        return this.generateAccessToken(claims, userId);
    }

    private String generateAccessToken(Map<String, Object> claims, Integer userId) {
        return Jwts.builder()
                .claims(claims)
                .subject(userId.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(this.getAccessTokenSecretKey())
                .compact();
    }

    public Integer extractUserIdFromExpiredAccessToken(String token) {
        try {
            return Integer.parseInt(
                    Jwts.parser()
                        .verifyWith(this.getAccessTokenSecretKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject()
            );
        } catch (ExpiredJwtException exception) {
            return Integer.parseInt(exception.getClaims().getSubject());
        }
    }

    public Integer extractUserIdFromRefreshToken(String refreshToken) {
        return Integer.parseInt(
                Jwts.parser()
                    .verifyWith(this.getRefreshTokenSecretKey())
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload()
                    .getSubject()
        );
    }

    public Integer extractUserIdFromAccessToken(String jwt) {
        return Integer.parseInt(extractClaim(jwt, Claims::getSubject));
    }

    public AccessTokenClaim parseToken(String accessToken) {
        final Claims claims = this.extractAllClaims(accessToken);

        long userId = Long.parseLong(claims.getSubject());
        long departmentId = Long.parseLong(claims.get("departmentId").toString());
        String role = claims.get("role").toString();

        return AccessTokenClaim.builder()
                .roleCode(role)
                .userId(userId)
                .departmentId(departmentId)
                .build();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.getAccessTokenSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getAccessTokenSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(ACCESS_TOKEN_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getRefreshTokenSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_REFRESH_TOKEN_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}