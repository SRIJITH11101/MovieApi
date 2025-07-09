package com.moviesprime.cineapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviesprime.cineapi.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    // This interface extends JpaRepository to provide CRUD operations for the Movie entity.
    // It uses Movie as the entity type and Integer as the ID type.
    // No additional methods are defined here, as JpaRepository provides all necessary methods.

}
