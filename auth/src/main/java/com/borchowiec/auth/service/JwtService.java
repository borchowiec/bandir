package com.borchowiec.auth.service;

import com.borchowiec.auth.dto.AuthenticationToken;
import com.borchowiec.remote.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

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

        return new AuthenticationToken("Bearer", token);
    }
}
