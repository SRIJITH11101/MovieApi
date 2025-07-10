package com.moviesprime.cineapi.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.moviesprime.cineapi.dto.MovieDto;

public interface MovieService {

    MovieDto addMovie(MovieDto moviedto,MultipartFile file) throws IOException;

    MovieDto getMovie(Integer movieId);

    List<MovieDto> getAllMovies();
}
