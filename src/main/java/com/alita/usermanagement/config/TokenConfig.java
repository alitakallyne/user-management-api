package com.alita.usermanagement.config;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.alita.usermanagement.infrastructure.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


@Component
public class TokenConfig {

    private String secret = "mysecretkey";
    

    public String generateToken(User user) {
         Algorithm algorithm = Algorithm.HMAC256(secret);
     
        return JWT.create()
            .withClaim("userId", user.getId())
            .withSubject(user.getEmail())
            .withExpiresAt(Instant.now().plusSeconds(86400000)) // 1 day expiration
            .withIssuedAt(Instant.now())
            .sign(algorithm);
    }

}
