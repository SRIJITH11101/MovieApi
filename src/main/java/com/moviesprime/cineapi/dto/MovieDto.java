package com.moviesprime.cineapi.dto;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    
    private Integer id;

    @NotBlank(message = "Title is required")
    private String title;

    
    @NotBlank(message = "Dirctor name is required")
    private String director;

    
    @NotBlank(message = "Movie's studio is required")
    private String studio;

    private Set<String> movieCast;

    private Integer releaseYear;

    @NotBlank(message = "Poster is required")
    private String poster;

    @NotBlank(message = "Poster's Url is required")
    private String posterUrl;

}
