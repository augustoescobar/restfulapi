package com.example.restfulapi.services;

import com.example.restfulapi.entities.Authority;
import com.example.restfulapi.repositiories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorityService {

    private AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(
            AuthorityRepository authorityRepository) {

        this.authorityRepository = authorityRepository;
    }

    public Set<Authority> findAll() {

        Set<Authority> authorities = new HashSet<>();

        authorityRepository.findAll().forEach(authorities::add);

        return authorities;
    }

    public void save(Authority authority) {

        authorityRepository.save(authority);
    }

    public Long count() {

        return authorityRepository.count();
    }

    public Optional<Authority> findByName(String authority) {

        return authorityRepository.findByAuthority(authority);
    }
}
