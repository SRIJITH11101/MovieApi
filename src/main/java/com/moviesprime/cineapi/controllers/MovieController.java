package com.moviesprime.cineapi.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviesprime.cineapi.dto.MovieDto;
import com.moviesprime.cineapi.services.MovieService;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto) throws IOException{
        MovieDto dto = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
        
    }

    private MovieDto convertToMovieDto(String MovieDtoObj) throws JsonMappingException, JsonProcessingException{
        // This method converts a JSON string representation of a MovieDto object into a MovieDto instance.
        ObjectMapper objectMapper = new ObjectMapper();
        
        return objectMapper.readValue(MovieDtoObj, MovieDto.class);
       
    }
}

