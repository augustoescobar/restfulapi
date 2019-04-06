package com.example.restfulapi.filters;

import com.example.restfulapi.entities.AuthenticationToken;
import com.example.restfulapi.entities.User;
import com.example.restfulapi.services.AuthenticationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
public class StatelessAuthenticationFilter extends GenericFilterBean {

    private AuthenticationTokenService authenticationTokenService;

    @Autowired
    public StatelessAuthenticationFilter(
            AuthenticationTokenService authenticationTokenService) {

        this.authenticationTokenService = authenticationTokenService;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        Optional<AuthenticationToken> optionalToken = authenticationTokenService.findByRequest((HttpServletRequest) request);

        if (optionalToken.isPresent()) {

            AuthenticationToken token = optionalToken.get();

            authenticationTokenService.isValid(token);

            User user = token.getUser();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);

        SecurityContextHolder.getContext().setAuthentication(null);
    }
}