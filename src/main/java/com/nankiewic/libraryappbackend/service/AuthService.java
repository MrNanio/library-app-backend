package com.nankiewic.libraryappbackend.service;

import com.nankiewic.libraryappbackend.dto.auth.AuthRequest;
import com.nankiewic.libraryappbackend.dto.auth.AuthResponse;
import com.nankiewic.libraryappbackend.exception.UsernameAlreadyTakenException;
import com.nankiewic.libraryappbackend.jwt.JwtUtility;
import com.nankiewic.libraryappbackend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest authRequest) {

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.getUserByEmail(authRequest.getEmail());

        String jwtToken = JwtUtility.generateJwtToken(userDetails);

        return AuthResponse.builder()
                .jwtToken(jwtToken)
                .email(userDetails.getUsername())
                .userId(user.getId().toString())
                .roleName(user.getRole().getName())
                .build();
    }

    public void register(AuthRequest authRequest) {

        if (userService.userExistByEmail(authRequest.getEmail())) {
            throw new UsernameAlreadyTakenException("username is already taken");
        }
        User user = User.builder()
                .email(authRequest.getEmail())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .build();
        userService.saveUser(user);
    }
}