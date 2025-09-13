package com.moviesprime.cineapi.auth.services;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.moviesprime.cineapi.auth.entities.User;
import com.moviesprime.cineapi.auth.entities.UserRole;
import com.moviesprime.cineapi.auth.repositories.UserRepository;
import com.moviesprime.cineapi.auth.utils.AuthResponse;
import com.moviesprime.cineapi.auth.utils.LoginRequest;
import com.moviesprime.cineapi.auth.utils.RegisterRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse registerUser(RegisterRequest registerRequest){
        var user = User.builder()
             .name(registerRequest.getName())
             .email(registerRequest.getEmail())
             .username(registerRequest.getUsername())
             .password(passwordEncoder.encode(registerRequest.getPassword()))
             .role(UserRole.USER)
             .build();

        User savedUser = userRepository.save(user);

        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

        return AuthResponse.builder()
             .accessToken(accessToken)
             .refreshToken(refreshToken.getRefreshToken())
             .build();
    }


    public AuthResponse loginUser(LoginRequest loginRequest){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );
        var user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return AuthResponse.builder()
             .accessToken(accessToken)
             .refreshToken(refreshToken.getRefreshToken())
             .build();
    }
}
