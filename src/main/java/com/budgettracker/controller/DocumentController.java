package com.budgettracker.controller;

import com.budgettracker.model.UploadedFile;
import com.budgettracker.model.User;
import com.budgettracker.repository.UploadedFileRepository;
import com.budgettracker.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.FileInputStream;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    @Autowired
    private UploadedFileRepository uploadedFileRepository; // repo for file info
    @Autowired
    private UserRepository userRepository; // repo for users

    // POST endpoint for file upload
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) {
        // Get current username from auth
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("User not found");
        User user = userOpt.get();
        // Define uploads folder path (absolute, project root)
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs(); // Create folder if not exists
        try {
            // Save file to uploads folder
            File dest = new File(dir, file.getOriginalFilename());
            file.transferTo(dest);
            // Save file info to DB
            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setFilename(file.getOriginalFilename());
            uploadedFile.setFilepath(dest.getAbsolutePath());
            uploadedFile.setUser(user);
            uploadedFile.setUploadDate(LocalDateTime.now());
            uploadedFileRepository.save(uploadedFile); // save to DB
            // Return success message
            return ResponseEntity.ok("Uploaded successfully");
        } catch (IOException e) {
            // Log error for debugging
            logger.error("File upload failed", e);
            return ResponseEntity.status(500).body("Upload has failed: " + e.getMessage());
        }
    }

    // GET endpoint to fetch all files for the authenticated user
    @GetMapping("/my-files")
    public ResponseEntity<?> getMyFiles(Authentication authentication) {
        String username = authentication.getName(); // get username from auth
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("User not found");
        User user = userOpt.get();
        // Fetch files for user
        return ResponseEntity.ok(uploadedFileRepository.findByUser(user));
    }

    // Download endpoint to serve file by id
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id, Authentication authentication) {
        Optional<UploadedFile> fileOpt = uploadedFileRepository.findById(id);
        if (fileOpt.isEmpty()) return ResponseEntity.notFound().build();
        UploadedFile file = fileOpt.get();
        // Only allow download if file belongs to user
        String username = authentication.getName();
        if (!file.getUser().getUsername().equals(username)) return ResponseEntity.status(403).body("Forbidden");
        try {
            File f = new File(file.getFilepath());
            if (!f.exists()) return ResponseEntity.notFound().build();
            InputStreamResource resource = new InputStreamResource(new FileInputStream(f));
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(f.length())
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Download failed: " + e.getMessage());
        }
    }
} 