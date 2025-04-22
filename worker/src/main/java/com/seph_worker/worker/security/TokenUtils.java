package com.seph_worker.worker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;

public class TokenUtils {

    private final static String ACCESS_TOKEN = "X\"qZ5:+c=]$8H2h3&y[pF^a;,P'6z7r)#}EQYCxAwU!VD~{Sg4";

    //private final static Long ACCESS_TOKEN_EXPIRES_IN = 30L; // Duración en segundos
   private final static Long ACCESS_TOKEN_EXPIRES_IN = 10_800L;

    public static String createToken(String user) {
        long expirationTime = ACCESS_TOKEN_EXPIRES_IN * 1_000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setSubject(user)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN.getBytes()))
                .compact();
    }

    public static Object getAuthorization(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(ACCESS_TOKEN.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String user = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return "Token expirado";
        } catch (JwtException e) {
            return "Token inválido";
        }
    }
}
