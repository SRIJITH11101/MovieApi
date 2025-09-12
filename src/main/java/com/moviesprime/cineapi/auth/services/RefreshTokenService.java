package com.moviesprime.cineapi.auth.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moviesprime.cineapi.auth.entities.RefreshToken;
import com.moviesprime.cineapi.auth.entities.User;
import com.moviesprime.cineapi.auth.repositories.RefreshTokenRepository;
import com.moviesprime.cineapi.auth.repositories.UserRepository;

@Service
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    public RefreshToken creatRefreshToken(String username){
        User user = userRepository.findByEmail(username)
            .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        
            RefreshToken refreshToken = user.getRefreshToken(); 
            if(refreshToken==null){
                long refreshTokenValidity = 5 * 60 * 60 * 10000;
                refreshToken = RefreshToken.builder()
                                 .refreshToken(UUID.randomUUID().toString())
                                 .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                                 .user(user)
                                 .build();
                refreshTokenRepository.save(refreshToken);
            }
            return refreshToken;

    }


    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken refToken =refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("refresh token not found"));

        if(refToken.getExpirationTime().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("refresh token expired");
        }

        return refToken;
    }

}
