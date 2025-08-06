package com.shubham.backend.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Value("${jwt.token.prefix}")
    private String tokenPrefix;

    @Value("${jwt.header}")
    private String headerName;

    /**
     * Extract username from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a specific claim from the token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(removeTokenPrefix(token))
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new JwtException("JWT token is expired", ex);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException ex) {
            throw new JwtException("Invalid JWT token", ex);
        } catch (IllegalArgumentException ex) {
            throw new JwtException("JWT claims string is empty", ex);
        }
    }

    /**
     * Remove token prefix (e.g., "Bearer ")
     */
    public String removeTokenPrefix(String token) {
        if (StringUtils.hasText(token) && token.startsWith(tokenPrefix)) {
            return token.substring(tokenPrefix.length());
        }
        return token;
    }

    /**
     * Check if token is expired
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generate token for user
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generate token with extra claims
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + jwtExpirationInMs);

        // Add user roles to the token claims
        if (extraClaims == null) {
            extraClaims = new HashMap<>();
        }

        // Add user roles to the token
        if (userDetails.getAuthorities() != null) {
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            extraClaims.put("roles", roles);
        }

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Get signing key from secret
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Get token expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        return extractExpiration(removeTokenPrefix(token));
    }

    /**
     * Get username from token
     */
    public String getUsernameFromToken(String token) {
        return extractUsername(removeTokenPrefix(token));
    }

    /**
     * Check if token is valid for the given user
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Generate a refresh token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return generateToken(claims, userDetails);
    }

    /**
     * Get token prefix
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }

    /**
     * Get header name
     */
    public String getHeaderName() {
        return headerName;
    }
}
