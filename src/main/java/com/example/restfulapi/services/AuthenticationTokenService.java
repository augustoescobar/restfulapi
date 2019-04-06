package com.example.restfulapi.services;

import com.example.restfulapi.entities.AuthenticationToken;
import com.example.restfulapi.entities.User;
import com.example.restfulapi.repositiories.AuthenticationTokenRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationTokenService {

    private static final String AUTH_HEADER_NAME = "Authorization";

    private AuthenticationTokenRepository authenticationTokenRepository;

    public AuthenticationTokenService(
            AuthenticationTokenRepository authenticationTokenRepository) {

        this.authenticationTokenRepository = authenticationTokenRepository;
    }

    public AuthenticationToken createOrUpdate(User user) {

        Optional<AuthenticationToken> optionalToken = authenticationTokenRepository.findByUser(user);

        AuthenticationToken token;

        if (optionalToken.isPresent()) {

            token = optionalToken.get();
            token.setToken(UUID.randomUUID().toString());
            token.setExpiresIn(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
        } else {

            token = new AuthenticationToken();
            token.setToken(UUID.randomUUID().toString());
            token.setCreatedDate(LocalDateTime.now());
            token.setExpiresIn(token.getCreatedDate().plus(1, ChronoUnit.DAYS));
            token.setUser(user);
        }

        authenticationTokenRepository.save(token);

        return token;
    }

    public Boolean isValid(AuthenticationToken authenticationToken) {

        return LocalDateTime.now().isBefore(authenticationToken.getExpiresIn());
    }

    public Boolean isValid(String token) {

        Optional<AuthenticationToken> optionalToken = authenticationTokenRepository.findByToken(token);

        if (optionalToken.isPresent()) {

            return isValid(optionalToken.get());
        }

        return false;
    }

    public Boolean isValid(HttpServletRequest request) {

        String token = request.getHeader(AUTH_HEADER_NAME);

        return token != null ? isValid(token) : false;
    }

    public Optional<AuthenticationToken> findByToken(String token) {

        return authenticationTokenRepository.findByToken(token);
    }

    public Optional<AuthenticationToken> findByRequest(HttpServletRequest request) {

        String token = request.getHeader(AUTH_HEADER_NAME);

        return authenticationTokenRepository.findByToken(token);
    }

    public void deleteByToken(String token) {

        Optional<AuthenticationToken> optionalToken = authenticationTokenRepository.findByToken(token);

        optionalToken.ifPresent(authenticationToken -> authenticationTokenRepository.delete(authenticationToken));
    }

    public void logout(HttpServletRequest request) {

        String token = request.getHeader(AUTH_HEADER_NAME);

        if (token != null) deleteByToken(token);
    }
}
