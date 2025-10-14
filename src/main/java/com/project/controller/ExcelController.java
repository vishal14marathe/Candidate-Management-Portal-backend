package com.project.controller;

import com.project.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/candidates")
public class ExcelController {
    
    @Autowired
    private ExcelService excelService;
    
    @PostMapping("/import-excel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importCandidates(@RequestParam("file") MultipartFile file) {
        try {
            List<String> errors = excelService.importCandidatesFromExcel(file);
            if (errors.isEmpty()) {
                return ResponseEntity.ok("All candidates imported successfully");
            } else {
                return ResponseEntity.ok(new ImportResponse("Import completed with errors", errors));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error importing file: " + e.getMessage());
        }
    }
    
    @GetMapping("/export-excel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ByteArrayResource> exportCandidates(
            @RequestParam(required = false) String qualification,
            @RequestParam(required = false) String occupationStatus,
            @RequestParam(required = false) String location) {
        try {
            byte[] data = excelService.exportCandidatesToExcel(qualification, occupationStatus, location);
            ByteArrayResource resource = new ByteArrayResource(data);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=candidates.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(data.length)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    static class ImportResponse {
        public String message;
        public List<String> errors;
        
        public ImportResponse(String message, List<String> errors) {
            this.message = message;
            this.errors = errors;
        }
    }
}
