package com.moviesprime.cineapi.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviesprime.cineapi.dto.MovieDto;
import com.moviesprime.cineapi.exceptions.EmptyFileException;
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
        if(file.isEmpty()) {
            throw new EmptyFileException("File cannot be empty. Please provide a valid file.");
        }
        MovieDto dto = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
        
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId) {
        MovieDto movie = movieService.getMovie(movieId);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @GetMapping("/all-movies")
    public ResponseEntity<List<MovieDto>> getAllMoviesHandler() {
        List<MovieDto> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @PutMapping("/update-movie/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId, @RequestPart MultipartFile file, @RequestPart String movieDto) throws IOException {
        if (file.isEmpty()) {
            file = null; // If no file is provided, set it to null
        }
        MovieDto dto = convertToMovieDto(movieDto);
        MovieDto updatedMovie = movieService.updateMovie(movieId, dto, file);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("/delete-movie/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        String response = movieService.deleteMovie(movieId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }






    private MovieDto convertToMovieDto(String MovieDtoObj) throws JsonMappingException, JsonProcessingException{
        // This method converts a JSON string representation of a MovieDto object into a MovieDto instance.
        ObjectMapper objectMapper = new ObjectMapper();
        
        return objectMapper.readValue(MovieDtoObj, MovieDto.class);
       
    }


}

