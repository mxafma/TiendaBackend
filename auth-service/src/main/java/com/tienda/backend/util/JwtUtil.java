package com.tienda.backend.util;

import com.tienda.backend.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret:ChangeThisSecretForProdChangeThisSecretForProd}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long jwtExpirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        // Create HMAC key from secret (ensure sufficient length)
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Usuario user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("rol", user.getRol())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
