package com.budgettracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entity for uploaded files
@Entity
public class UploadedFile {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generates unique id for each file
    private Long id; // unique file ID

    private String filename; // original file name
    private String filepath; // path in uploads folder

    @ManyToOne // many files can belong to one user
    @JoinColumn(name = "user_id")
    private User user; // owner of the file

    private LocalDateTime uploadDate; // when uploaded

    // Getters and setters
    public Long getId() { return id; } // get the id of the file
    public void setId(Long id) { this.id = id; }

    public String getFilename() { return filename; } // get the name of the file
    public void setFilename(String filename) { this.filename = filename; } // set the name of the file

    public String getFilepath() { return filepath; } // get the path of the file
    public void setFilepath(String filepath) { this.filepath = filepath; } // set the path of the file

    public User getUser() { return user; } // get the user of the file
    public void setUser(User user) { this.user = user; } // set the user of the file

    public LocalDateTime getUploadDate() { return uploadDate; } // get the upload date of the file
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; } // set the upload date of the file
} 