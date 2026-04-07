package com.alita.usermanagement.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alita.usermanagement.infrastructure.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;


@Component
public class TokenConfig {

    @Value("${jwt.secret}")
    private String secret;
    

    public String generateToken(User user) {
         Algorithm algorithm = Algorithm.HMAC256(secret);
     
        return JWT.create()
            .withClaim("userId", user.getId())
            .withClaim("roles", user.getRoles().stream().map(Enum::name).toList())
            .withSubject(user.getEmail())
            .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
            .withIssuedAt(Instant.now())
            .sign(algorithm);
    }


    public Optional<JWTUserData> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT decoder = JWT.require(algorithm)
                .build().verify(token);
            
            return Optional.of(JWTUserData.builder()
                    .userId(decoder.getClaim("userId").asLong())
                    .email(decoder.getSubject())
                    .roles(decoder.getClaim("roles").asList(String.class))
                    .build());
        } catch (JWTVerificationException ex) {
           return Optional.empty();
        }

       

    }


    public String generateRefreshToken() {
         return UUID.randomUUID().toString();
    }
    

}
