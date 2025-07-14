package com.moviesprime.cineapi.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

    // This class implements the FileService interface.
    // It will contain methods to upload files and retrieve resources.
    // The actual implementation of these methods will be provided later.

    @Override
    public String uplaodFile(String path, MultipartFile file) throws IOException {
        // Implementation for uploading a file
        // Get the file name
        String fileName = file.getOriginalFilename();
        
        // Get the full file path
        String filePath = path + File.separator + fileName;

        //Create file object for the directory
        File f = new File(path);
        if(!f.exists()) {
            f.mkdirs(); // Create directories if they do not exist
        }

        // Save the file to the specified path
        // Using Files.copy to copy the file from the MultipartFile input stream to the file system

        // 3 parameters: Input stream, destination path, and copy option
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        // Implementation for retrieving a resource file

        // Get the full file path
        String filePath = path + File.separator + fileName;

        return new FileInputStream(filePath); // Placeholder return statement
    }

}
