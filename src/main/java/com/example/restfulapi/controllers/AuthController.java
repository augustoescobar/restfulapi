package com.example.restfulapi.controllers;

import com.example.restfulapi.dtos.LoginRequestDTO;
import com.example.restfulapi.dtos.LoginResponseDTO;
import com.example.restfulapi.entities.AuthenticationToken;
import com.example.restfulapi.entities.User;
import com.example.restfulapi.services.AuthenticationTokenService;
import com.example.restfulapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private UserService userService;
    private AuthenticationTokenService authenticationTokenService;

    @Autowired
    public AuthController(
            UserService userService,
            AuthenticationTokenService authenticationTokenService) {

        this.userService = userService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO) {

        User user = userService.loadUserByUsername(requestDTO.getUsername());

        if (!userService.isValid(user, requestDTO.getPassword())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AuthenticationToken token = authenticationTokenService.createOrUpdate(user);

        LoginResponseDTO responseDTO = new LoginResponseDTO(token);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        authenticationTokenService.logout(request);

        return ResponseEntity.noContent().build();
    }
}
