package com.e_commerce.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.key.private}")
    private String privateKey;
    @Value("${jwt.user.generator}")
    private String userGenerator;

    public String createToken(@NotNull Authentication authentication) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            String username = authentication.getName();
            String authorities = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            return JWT.create()
                    .withIssuer(this.userGenerator)
                    .withSubject(username)
                    .withClaim("authorities", authorities)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
                    .withJWTId(UUID.randomUUID().toString())
                    .withNotBefore(new Date(System.currentTimeMillis()))
                    .sign(algorithm);

        } catch(JWTCreationException exception){
            log.error("Error creating token", exception);

            throw new RuntimeException("Error creating token", exception);
        }
    }

    public DecodedJWT verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token invalid, not authorized");
        }
    }

    public String extractUsername(@NotNull DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(@NotNull DecodedJWT decodedJWT, String claim) {
        return decodedJWT.getClaim(claim);
    }
}
