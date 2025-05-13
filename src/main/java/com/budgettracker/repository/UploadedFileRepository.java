package com.budgettracker.repository;

import com.budgettracker.model.UploadedFile;
import com.budgettracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repository for UploadedFile entity
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
    List<UploadedFile> findByUser(User user); // find files by user
    void deleteByUser(User user); // Delete all files for a user
} 