package com.donald.service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtUtil {

    @Value("${donald'sApp.jwtSecret}")
    private String jwtSecret;

    @Value("${donald'sApp.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${donald'sApp.jwtRefreshExpirationMs}")
    private Long refreshTokenDuration;

    private static final Logger logger = LogManager.getLogger("com.donald.service.security");

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    // nqs username qe marr nga user dhe username qe marr nga token perputhen && token not expired => valid token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date()); //pra returns true nqs expired
    }


    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public String generateTokenFromUserDetails(UserDetails userDetails) {
        return tokenBuilder(userDetails.getUsername());
    }
    // useful when user will provide refresh token, in order to get a new access token
    public String generateTokenWithUsername(String username) {
        return tokenBuilder(username);
    }

    public Long getRefreshTokenDuration() {
        return refreshTokenDuration;
    }

    private String tokenBuilder(String username) {
        Map<String, Object> claims = new HashMap<>();
        logger.debug("Generating token");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // 1h
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
