package com.dermascan.api.payload;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScanResponse {
    private Long id;
    private String imagePath;
    private String analysisResult;
    private String bodyLocation;
    private LocalDateTime timestamp;

    public ScanResponse(Long id, String imagePath, String analysisResult, String bodyLocation,
            LocalDateTime timestamp) {
        this.id = id;
        this.imagePath = imagePath;
        this.analysisResult = analysisResult;
        this.bodyLocation = bodyLocation;
        this.timestamp = timestamp;
    }
}
