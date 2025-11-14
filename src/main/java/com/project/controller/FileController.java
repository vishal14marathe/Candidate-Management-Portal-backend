package com.project.controller;

import com.project.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    
    @Autowired
    private FileStorageService fileStorageService;
    
    // Public endpoint for file upload during registration (no authentication needed)
    @PostMapping("/upload-temp")
    public ResponseEntity<?> uploadTempFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileStorageService.storeTempFile(file);
            return ResponseEntity.ok(new FileUploadResponse(fileName, "File uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new FileUploadResponse(null, "Failed to upload file: " + e.getMessage()));
        }
    }
    
    @PostMapping("/upload-resume/{id}")
    public ResponseEntity<String> uploadResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file, id);
        return ResponseEntity.ok("Resume uploaded successfully: " + fileName);
    }
    
    @GetMapping("/download-resume/{fileName}")
    public ResponseEntity<Resource> downloadResume(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    // Response DTO
    static class FileUploadResponse {
        private String filePath;
        private String message;
        
        public FileUploadResponse(String filePath, String message) {
            this.filePath = filePath;
            this.message = message;
        }
        
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
