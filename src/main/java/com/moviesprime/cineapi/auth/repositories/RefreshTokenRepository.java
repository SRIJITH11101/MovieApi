package com.moviesprime.cineapi.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviesprime.cineapi.auth.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer>{

}
