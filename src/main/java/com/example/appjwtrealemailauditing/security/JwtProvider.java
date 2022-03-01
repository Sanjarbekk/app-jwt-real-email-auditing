package com.example.appjwtrealemailauditing.security;

import com.example.appjwtrealemailauditing.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

    public static final long expireTime = 1000 * 60 * 60 * 24;
    public static final String secretKey = "secretKey";

    public String generateToken(String email, Set<Role> roles){
        Date expireDate =  new Date(System.currentTimeMillis() + expireTime);
        String token =
                Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return token;
    }
}
