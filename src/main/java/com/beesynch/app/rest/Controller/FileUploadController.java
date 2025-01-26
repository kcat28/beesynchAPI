package com.beesynch.app.rest.Controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Value("${upload.path}")
    private String uploadPath;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // Step 1: Generate a random filename
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            String randomFileName = UUID.randomUUID().toString() + fileExtension;

            // Step 2: Save the file to the server
            Path path = Paths.get(uploadPath + File.separator + randomFileName);
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);

            // Step 3: Return the random filename in the response
            return ResponseEntity.ok().body("{\"fileName\": \"" + randomFileName + "\"}");
        } catch (IOException e) {
            // Log the exception
            e.printStackTrace(); //error details
            System.out.println("Error during file upload: " + e.getMessage());

            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }
}
