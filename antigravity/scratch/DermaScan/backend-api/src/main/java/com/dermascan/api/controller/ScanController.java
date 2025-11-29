package com.dermascan.api.controller;

import com.dermascan.api.model.Scan;
import com.dermascan.api.repository.ScanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/scans")
public class ScanController {

    @Autowired
    ScanRepository scanRepository;

    private final Path root = Paths.get("uploads");

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(filename));

            // TODO: Link to actual user
            Scan scan = new Scan();
            scan.setImagePath(filename);
            scan.setAnalysisResult("Pending");
            
            // Temporary: Save without user for test (will fail due to nullable=false)
            // scanRepository.save(scan);

            return ResponseEntity.ok("Image uploaded successfully: " + filename);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Could not upload the file: " + e.getMessage());
        }
    }
}
