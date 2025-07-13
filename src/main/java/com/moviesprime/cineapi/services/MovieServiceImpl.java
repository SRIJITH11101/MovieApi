package com.moviesprime.cineapi.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moviesprime.cineapi.dto.MovieDto;
import com.moviesprime.cineapi.entities.Movie;
import com.moviesprime.cineapi.repositories.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;
    private final FileService fileService;
    @Value("${project.poster}")
    private String path;
    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto moviedto, MultipartFile file) throws IOException {
        //1. Upload the file
        String uploadedFileName = fileService.uplaodFile(path, file);
        //2. set the value of field 'poster' as file name
        moviedto.setPoster(uploadedFileName); 
        //3. Map the MovieDto to Movie entity
        Movie movie = new Movie(
           null,
            moviedto.getTitle(),
            moviedto.getDirector(),
            moviedto.getStudio(),
            moviedto.getMovieCast(),
            moviedto.getReleaseYear(),
            moviedto.getPoster()
        );
        //4. Save the movie entity to the database
        Movie savedMovie = movieRepository.save(movie);
        //5. Generate the poster URL
        String posterUrl =baseUrl+"/file/"+ uploadedFileName; // Adjust the URL as needed
        //6. Map the saved movie entity to MovieDto and return it

        MovieDto response = new MovieDto(
            savedMovie.getId(),
            savedMovie.getTitle(),
            savedMovie.getDirector(),
            savedMovie.getStudio(),
            savedMovie.getMovieCast(),
            savedMovie.getReleaseYear(),
            savedMovie.getPoster(),
            posterUrl
        );
        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        //Check if the data in DB and if exits , fetch the data of the given id
        Movie movie=movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));
        //Generate the poster URL
        String posterUrl = baseUrl + "/file/" + movie.getPoster(); // Adjust the URL as needed
        //Map the Movie entity to MovieDto and return it
         MovieDto response = new MovieDto(
            movie.getId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getStudio(),
            movie.getMovieCast(),
            movie.getReleaseYear(),
            movie.getPoster(),
            posterUrl
        );
        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        //To fetch all the movies from the database
        List<Movie> movies = movieRepository.findAll();

        List<MovieDto> moviesDto = new ArrayList<>();
        //Interate through the list, generate posterUrl for each movie obj,
        //Map to MovieDto and return the list
        for(Movie movie : movies){
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto response = new MovieDto(
            movie.getId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getStudio(),
            movie.getMovieCast(),
            movie.getReleaseYear(),
            movie.getPoster(),
            posterUrl
         );
            moviesDto.add(response);
        }
        return moviesDto;
    }
    

}
