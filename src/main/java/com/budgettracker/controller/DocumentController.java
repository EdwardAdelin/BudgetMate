package com.budgettracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    // POST endpoint for file upload
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // Define uploads folder path (absolute, project root)
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs(); // Create folder if not exists
        try {
            // Save file to uploads folder
            File dest = new File(dir, file.getOriginalFilename());
            file.transferTo(dest);
            // Return success message
            return ResponseEntity.ok("Uploaded successfully");
        } catch (IOException e) {
            // Log error for debugging
            logger.error("File upload failed", e);
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }
} 