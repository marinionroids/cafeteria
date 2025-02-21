package com.marin.cafeteria.config.jwt;

import com.marin.cafeteria.model.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "0vwaZQMxY8qJubVDcASrxWSRkgVmAnWT";
    private static final long EXPIRATION_TIME = 86_400_000;
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());







    // Generates a JWT token!
    public String generateToken(Employee employee) {

        return Jwts.builder()
                .setSubject(employee.getUsername())
                .claim("role", employee.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME ))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    // returns username
    public String validateToken(String token) {
        if(token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try{

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (claims.getExpiration().before(new Date())) {
                return null;
            }
            return claims.getSubject(); // returns walletAddress.

        } catch (Exception e) {
            return null;
        }
    }

}
