package com.example.restfulapi.services;

import com.example.restfulapi.entities.User;
import com.example.restfulapi.repositiories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (!optionalUser.isPresent()) {

            throw new UsernameNotFoundException(username);
        }

        return optionalUser.get();
    }

    public Page<User> findAll(Pageable pageable) {

        return userRepository.findAll(pageable);
    }

    public long count() {

        return userRepository.count();
    }

    public void save(User user) {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (user.getId() == null) {
//
//            user.setCreatedDate(LocalDateTime.now());
//
//            user.setModifiedDate(user.getCreatedDate());
//
//            if (authentication != null) {
//
//                user.setCreatedBy(authentication.getName());
//
//                user.setModifiedBy(authentication.getName());
//            }
//        } else {
//
//            user.setModifiedDate(LocalDateTime.now());
//
//            if (authentication != null) {
//
//                user.setModifiedBy(authentication.getName());
//            }
//        }

        userRepository.save(user);
    }

    public boolean isValid(User user, String password) {

        return user != null
                && user.isEnabled()
                && user.isAccountNonExpired()
                && user.isAccountNonLocked()
                && user.isCredentialsNonExpired()
                && passwordEncoder.matches(password, user.getPassword());
    }

    public String encodePassword(String rawPassword) {

        return passwordEncoder.encode(rawPassword);
    }
}
