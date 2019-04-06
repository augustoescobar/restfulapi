package com.example.restfulapi.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class PersistanceConfig {

    @Component
    class SpringSecurityAuditorAware implements AuditorAware<String> {

        public Optional<String> getCurrentAuditor() {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            return Optional.of(authentication.getName());
        }
    }
}
