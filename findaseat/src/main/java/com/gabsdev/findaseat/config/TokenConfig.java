package com.gabsdev.findaseat.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gabsdev.findaseat.dto.JWTUserData;
import com.gabsdev.findaseat.model.entity.Token;
import com.gabsdev.findaseat.model.entity.User;
import com.gabsdev.findaseat.repository.TokenRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Configuration
public class TokenConfig {
    private final TokenRepository tokenRepository;

    @Value("${config.secret}")
    private String secret;
    Algorithm algorithm;

    public TokenConfig(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(User user) {

        String token = JWT.create()
                .withClaim("userID", user.getId().toString())
                .withClaim("businessName", user.getEmployees().getBusiness().getBusinessName())
                .withClaim("businessID", user.getEmployees().getBusiness().getUuid().toString())
                .withClaim("roles", user.getRoles())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(30 * 60))
                .withIssuedAt(Instant.now())
                .sign(algorithm);


        Token tokenEntity = new Token();
        tokenEntity.setTokenString(token);
        tokenRepository.save(tokenEntity);
        return token;


    }

    public Optional<JWTUserData> validadeToken(String token) {
        if (!tokenRepository.existsByTokenString(token)) {
            throw new JWTVerificationException("Token invalido");
        }
        Algorithm algorithm = Algorithm.HMAC256(secret);
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);

        return Optional.of(JWTUserData.builder()
                .uuid(UUID.fromString(decodedJWT.getClaim("userID").asString()))
                .email(decodedJWT.getSubject())
                .roles(decodedJWT.getClaim("roles").asList(String.class))
                .build());
    }
}
