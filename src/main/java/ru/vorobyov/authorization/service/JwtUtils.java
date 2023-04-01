package ru.vorobyov.authorization.service;

import io.jsonwebtoken.Claims;
import ru.vorobyov.authorization.entity.Role;
import ru.vorobyov.authorization.model.JwtAuthentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.*;

public class JwtUtils {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long expirationTime = 86400000; // 1 day in milliseconds

    public static JwtAuthentication generate(Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();

        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setAuthenticated(false);
        jwtAuthentication.setUsername((String) claims.get("username"));
        jwtAuthentication.setFirstName((String) claims.get("firstName"));

        Set<Role> roles = new HashSet<>();
        List<String> rolesAsString = (List<String>) claims.get("roles");
        if (rolesAsString != null) {
            for (String roleAsString : rolesAsString) {
                roles.add(Role.valueOf(roleAsString));
            }
        }
        jwtAuthentication.setRoles(roles);

        return jwtAuthentication;
    }
}
