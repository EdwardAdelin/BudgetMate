package com.budgettracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entity for uploaded files
@Entity
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // unique file ID

    private String filename; // original file name
    private String filepath; // path in uploads folder

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // owner of the file

    private LocalDateTime uploadDate; // when uploaded

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getFilepath() { return filepath; }
    public void setFilepath(String filepath) { this.filepath = filepath; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
} 