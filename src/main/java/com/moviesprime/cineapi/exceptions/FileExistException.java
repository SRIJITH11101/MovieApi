package com.moviesprime.cineapi.exceptions;

public class FileExistException extends RuntimeException {
    public FileExistException(String message) {
        super(message);
    }

}
