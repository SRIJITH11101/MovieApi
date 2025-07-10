package com.moviesprime.cineapi.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moviesprime.cineapi.services.FileService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file/")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // This annotation is used to inject the value of the property 'project.poster' from application.properties into the 'path' variable.
    @Value("${project.poster}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) throws IOException {
        // This method handles file upload requests.
        // It uses the FileService to upload the file and returns a response with the file name.
        String uploadedFileName = fileService.uplaodFile(path, file);
        return ResponseEntity.ok("File uploaed : " +uploadedFileName);
    }

    @GetMapping("/{fileName}")
    public void serveFileHandler (@PathVariable String fileName , HttpServletResponse response) throws IOException{

        InputStream resourceFile = fileService.getResourceFile(path, fileName);

        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resourceFile , response.getOutputStream());
    }
}
