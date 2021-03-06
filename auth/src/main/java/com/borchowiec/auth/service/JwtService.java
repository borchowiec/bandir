package com.borchowiec.auth.service;

import com.borchowiec.auth.dto.AuthenticationToken;
import com.borchowiec.remote.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final String TOKEN_TYPE = "Bearer";

    public AuthenticationToken getAuthenticationToken(User user) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, 24 * 7); // TODO read from configuration

        String token = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer("auth")
                .setSubject(user.getId())
                .setIssuedAt(date)
                .setExpiration(cal.getTime())
                .signWith(key)
                .compact();

        return new AuthenticationToken(TOKEN_TYPE, token);
    }

    public Jws<Claims> getClaims(String token) {
        if (!token.startsWith(TOKEN_TYPE)) {
            throw new JwtException("Wrong token type");
        }

        token = token.substring(TOKEN_TYPE.length() + 1);

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
