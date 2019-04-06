package com.example.restfulapi.repositiories;

import com.example.restfulapi.entities.AuthenticationToken;
import com.example.restfulapi.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationTokenRepository extends CrudRepository<AuthenticationToken, Long> {

    Optional<AuthenticationToken> findByToken(String token);

    Optional<AuthenticationToken> findByUser(User user);
}
