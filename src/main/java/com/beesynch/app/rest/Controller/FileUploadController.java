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
            // Step 1: Save the file to the server
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(uploadPath + File.separator + fileName);
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);

            // Step 2: Return the file name in the response
            return ResponseEntity.ok().body("{\"fileName\": \"" + fileName + "\"}");
        } catch (IOException e) {
            // Log the exception
            e.printStackTrace(); //error details
            System.out.println("Error during file upload: " + e.getMessage());

            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }
}
