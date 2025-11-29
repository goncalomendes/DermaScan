package com.dermascan.api.controller;

import com.dermascan.api.model.Scan;
import com.dermascan.api.model.User;
import com.dermascan.api.payload.ScanResponse;
import com.dermascan.api.repository.ScanRepository;
import com.dermascan.api.repository.UserRepository;
import com.dermascan.api.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scans")
public class ScanController {

    @Autowired
    ScanRepository scanRepository;

    @Autowired
    UserRepository userRepository;

    private final Path root = Paths.get("uploads");

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bodyLocation", required = false) String bodyLocation,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            // Create uploads directory if it doesn't exist
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            // Generate unique filename
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(filename));

            // Get the authenticated user
            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create and save scan record
            Scan scan = new Scan();
            scan.setUser(user);
            scan.setImagePath(filename);
            scan.setBodyLocation(bodyLocation);
            scan.setAnalysisResult("Pending"); // Will be updated by AI service later

            Scan savedScan = scanRepository.save(scan);

            // Return response
            ScanResponse response = new ScanResponse(
                    savedScan.getId(),
                    savedScan.getImagePath(),
                    savedScan.getAnalysisResult(),
                    savedScan.getBodyLocation(),
                    savedScan.getTimestamp());

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Could not upload the file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserScans(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            // Get all scans for the authenticated user
            List<Scan> scans = scanRepository.findByUserId(userDetails.getId());

            // Convert to response DTOs
            List<ScanResponse> responses = scans.stream()
                    .map(scan -> new ScanResponse(
                            scan.getId(),
                            scan.getImagePath(),
                            scan.getAnalysisResult(),
                            scan.getBodyLocation(),
                            scan.getTimestamp()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving scans: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScanById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Scan scan = scanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Scan not found"));

            // Verify the scan belongs to the authenticated user
            if (!scan.getUser().getId().equals(userDetails.getId())) {
                return ResponseEntity.status(403).body("Access denied");
            }

            ScanResponse response = new ScanResponse(
                    scan.getId(),
                    scan.getImagePath(),
                    scan.getAnalysisResult(),
                    scan.getBodyLocation(),
                    scan.getTimestamp());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
