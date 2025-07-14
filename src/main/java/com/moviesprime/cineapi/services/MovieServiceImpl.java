package com.moviesprime.cineapi.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        if(Files.exists(java.nio.file.Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new IOException("File already exists! Please use a different file name.");
        }
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

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        // Check if movie obj exists with the given id
        Movie mv=movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));

        // If file is null , use the existing poster
        //If file is not null, then delete existing file associated with the records
        // and upload the new file
        String fileName = mv.getPoster();
        if(file!=null){
            // Delete the existing file
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uplaodFile(path, file); // Upload the new file
        }

        //Set movieDto's poster value, according to prev step
        movieDto.setPoster(fileName);

        //map it to Movie entity
        Movie movie = new Movie(
            mv.getId(),
            movieDto.getTitle(),
            movieDto.getDirector(),
            movieDto.getStudio(),
            movieDto.getMovieCast(),
            movieDto.getReleaseYear(),
            movieDto.getPoster()
        );
        
        //Save the movie object -> return saved movie object
        Movie updatedMovie = movieRepository.save(movie);

        //generate the poster URL
        String posterUrl = baseUrl + "/file/" + fileName;

        // Map the saved movie entity to MovieDto and return it

        MovieDto response = new MovieDto(
            updatedMovie.getId(),
            updatedMovie.getTitle(),
            updatedMovie.getDirector(),
            updatedMovie.getStudio(),
            updatedMovie.getMovieCast(),
            updatedMovie.getReleaseYear(),
            updatedMovie.getPoster(),
            posterUrl
        );

        return response;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        // Check if movie obj exists with the given id
        Movie mv=movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));
        Integer id = mv.getId();
        //Delete the file associated with the movie
        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));

        //Delete the movie object
        movieRepository.delete(mv);

        return "Movie with id " + id + " deleted successfully.";
    }
    

}
