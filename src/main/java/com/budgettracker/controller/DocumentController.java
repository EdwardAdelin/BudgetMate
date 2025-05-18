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

@RestController // RestController annotation is used to create RESTful web services
@RequestMapping("/api/documents") // Maps HTTP requests to handler methods
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class); // Logger for logging messages
    @Autowired // Autowired annotation is used to inject the repository into the controller
    private UploadedFileRepository uploadedFileRepository; // repo for file info
    @Autowired // Autowired annotation is used to inject the repository into the controller
    private UserRepository userRepository; // repo for users

    // POST endpoint for file upload
    @PostMapping("/upload") // Maps HTTP POST requests to the uploadFile method
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) {
        // Get current username from auth
        String username = authentication.getName(); // Get the username of the current user
        Optional<User> userOpt = userRepository.findByUsername(username); // Find the user by username
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("User not found"); // If the user is not found, return an error
        User user = userOpt.get(); // Get the user
        // Define uploads folder path (absolute, project root)
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads"; // Get the path to the uploads folder
        File dir = new File(uploadDir); // Create a new file object for the uploads folder
        if (!dir.exists()) dir.mkdirs(); // Create folder if not exists
        try {
            // Save file to uploads folder
            File dest = new File(dir, file.getOriginalFilename()); // Create a new file object for the uploaded file
            file.transferTo(dest); // Transfer the file to the destination file
            // Save file info to DB
            UploadedFile uploadedFile = new UploadedFile(); // Create a new uploaded file object
            uploadedFile.setFilename(file.getOriginalFilename()); // Set the filename of the uploaded file
            uploadedFile.setFilepath(dest.getAbsolutePath()); // Set the path of the uploaded file
            uploadedFile.setUser(user); // Set the user of the uploaded file
            uploadedFile.setUploadDate(LocalDateTime.now()); // Set the upload date of the uploaded file
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
    @GetMapping("/my-files") // Maps HTTP GET requests to the getMyFiles method
    public ResponseEntity<?> getMyFiles(Authentication authentication) {
        String username = authentication.getName(); // get username from auth
        Optional<User> userOpt = userRepository.findByUsername(username); // Find the user by username
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("User not found"); // If the user is not found, return an error
        User user = userOpt.get(); // Get the user
        // Fetch files for user
        return ResponseEntity.ok(uploadedFileRepository.findByUser(user)); // Returns the files for the user
    }

    // Download endpoint to serve file by id
    @GetMapping("/download/{id}") // Maps HTTP GET requests to the downloadFile method
    public ResponseEntity<?> downloadFile(@PathVariable Long id, Authentication authentication) {
        Optional<UploadedFile> fileOpt = uploadedFileRepository.findById(id); // Find the file by id
        if (fileOpt.isEmpty()) return ResponseEntity.notFound().build(); // If the file is not found, return an error
        UploadedFile file = fileOpt.get(); // Get the file
        // Only allow download if file belongs to user
        String username = authentication.getName(); // Get the username of the current user
        if (!file.getUser().getUsername().equals(username)) return ResponseEntity.status(403).body("Forbidden"); // If the file does not belong to the user, return an error
        try {
            File f = new File(file.getFilepath()); // Create a new file object for the file
            if (!f.exists()) return ResponseEntity.notFound().build(); // If the file does not exist, return an error
            InputStreamResource resource = new InputStreamResource(new FileInputStream(f)); // Create a new input stream resource for the file
            return ResponseEntity.ok() // Returns the file
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"") // Set the content disposition for the file
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // Set the content type for the file
                .contentLength(f.length()) // Set the content length for the file
                .body(resource); // Returns the file
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Download failed: " + e.getMessage()); // If the file cannot be downloaded, return an error
        }
    }
} 