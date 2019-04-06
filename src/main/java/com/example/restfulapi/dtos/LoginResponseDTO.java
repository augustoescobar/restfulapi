package com.example.restfulapi.dtos;

import com.example.restfulapi.entities.AuthenticationToken;
import com.example.restfulapi.entities.Authority;

import java.util.Set;
import java.util.stream.Collectors;

public class LoginResponseDTO {

    private String token;

    private String createdAt;

    private String expiresIn;

    private String username;

    private Set<String> authorities;

    public LoginResponseDTO() { }

    public LoginResponseDTO(AuthenticationToken authenticationToken) {
        this.token = authenticationToken.getToken();
        this.createdAt = authenticationToken.getCreatedDate().toString();
        this.expiresIn = authenticationToken.getExpiresIn().toString();
        this.username = authenticationToken.getUser().getUsername();
        this.authorities = authenticationToken.getUser().getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toSet());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
}
