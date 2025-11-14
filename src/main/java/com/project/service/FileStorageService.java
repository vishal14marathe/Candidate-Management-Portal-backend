package com.project.service;

import com.project.config.FileStorageProperties;
import com.project.exception.FileStorageException;
import com.project.exception.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private boolean storageAvailable = false;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        // Try multiple directory options in order of preference
        Path[] possiblePaths = {
                Paths.get("/tmp/uploads/resumes"), // Render-compatible
                Paths.get(System.getProperty("java.io.tmpdir"), "uploads", "resumes"), // System temp
                Paths.get("uploads", "resumes"), // Relative path
                Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize() // Original config
        };

        Path selectedPath = null;
        Exception lastException = null;

        // Try each possible directory
        for (Path path : possiblePaths) {
            try {
                Files.createDirectories(path);

                // Test write permission by creating and deleting a test file
                Path testFile = path.resolve("write-test-" + System.currentTimeMillis() + ".tmp");
                Files.createFile(testFile);
                Files.write(testFile, "test".getBytes());
                Files.delete(testFile);

                selectedPath = path;
                this.storageAvailable = true;
                System.out.println("✓ File storage successfully initialized at: " + path.toAbsolutePath());
                break;

            } catch (Exception e) {
                lastException = e;
                System.out.println("✗ Cannot use directory " + path + ": " + e.getMessage());
                // Continue to next option
            }
        }

        // If no directory worked, use the first one but mark as unavailable
        if (selectedPath == null) {
            selectedPath = possiblePaths[0];
            System.out.println("⚠ WARNING: File storage may not be available. Using: " + selectedPath);
            this.storageAvailable = false;
        }

        this.fileStorageLocation = selectedPath;
    }

    /**
     * Store file in the system
     */
    public String storeFile(MultipartFile file) {
        if (!storageAvailable) {
            throw new FileStorageException("File storage is not available. Cannot store file.");
        }

        // Validate file
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("File is empty or null.");
        }

        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence: " + fileName);
            }

            // Check for empty filename
            if (fileName.trim().isEmpty()) {
                throw new FileStorageException("File name cannot be empty.");
            }

            // Generate unique file name to prevent overwriting
            String fileExtension = "";
            int lastDotIndex = fileName.lastIndexOf(".");
            if (lastDotIndex > 0) {
                fileExtension = fileName.substring(lastDotIndex);
            }
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // Copy file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File stored successfully: " + newFileName + " at " + targetLocation);
            return newFileName;

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Load file as Resource
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                throw new MyFileNotFoundException("File name cannot be null or empty");
            }

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found or not readable: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found: " + fileName, ex);
        }
    }

    /**
     * Delete file from storage
     */
    public boolean deleteFile(String fileName) {
        if (!storageAvailable) {
            System.out.println("Storage not available, cannot delete file: " + fileName);
            return false;
        }

        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                return false;
            }

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            boolean deleted = Files.deleteIfExists(filePath);

            if (deleted) {
                System.out.println("File deleted successfully: " + fileName);
            } else {
                System.out.println("File not found for deletion: " + fileName);
            }

            return deleted;
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file: " + fileName, ex);
        }
    }

    /**
     * Check if file exists
     */
    public boolean fileExists(String fileName) {
        if (!storageAvailable || fileName == null || fileName.trim().isEmpty()) {
            return false;
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.exists(filePath) && Files.isReadable(filePath);
        } catch (Exception ex) {
            System.out.println("Error checking file existence: " + fileName + " - " + ex.getMessage());
            return false;
        }
    }

    /**
     * Get file storage location
     */
    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }

    /**
     * Check if storage is available and writable
     */
    public boolean isStorageAvailable() {
        return storageAvailable;
    }

    /**
     * Get file size
     */
    public long getFileSize(String fileName) {
        if (!storageAvailable || !fileExists(fileName)) {
            return 0L;
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.size(filePath);
        } catch (IOException ex) {
            System.out.println("Error getting file size: " + fileName + " - " + ex.getMessage());
            return 0L;
        }
    }

    /**
     * Get file content type
     */
    public String getFileContentType(String fileName) {
        if (!storageAvailable || !fileExists(fileName)) {
            return null;
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.probeContentType(filePath);
        } catch (IOException ex) {
            System.out.println("Error getting file content type: " + fileName + " - " + ex.getMessage());
            return null;
        }
    }

    /**
     * Clean up old temporary files (optional maintenance method)
     */
    public void cleanupOldFiles(long olderThanMillis) {
        if (!storageAvailable) {
            return;
        }

        try {
            long cutoffTime = System.currentTimeMillis() - olderThanMillis;

            Files.list(this.fileStorageLocation)
                    .filter(path -> {
                        try {
                            return Files.isRegularFile(path) &&
                                    Files.getLastModifiedTime(path).toMillis() < cutoffTime;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            System.out.println("Cleaned up old file: " + path.getFileName());
                        } catch (IOException e) {
                            System.out.println("Failed to clean up file: " + path.getFileName());
                        }
                    });

        } catch (IOException ex) {
            System.out.println("Error during file cleanup: " + ex.getMessage());
        }
    }

    /**
     * Get storage statistics
     */
    public StorageStats getStorageStats() {
        if (!storageAvailable) {
            return new StorageStats(0, 0, false);
        }

        try {
            long fileCount = Files.list(this.fileStorageLocation)
                    .filter(Files::isRegularFile)
                    .count();

            long totalSize = Files.list(this.fileStorageLocation)
                    .filter(Files::isRegularFile)
                    .mapToLong(path -> {
                        try {
                            return Files.size(path);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();

            return new StorageStats(fileCount, totalSize, true);

        } catch (IOException ex) {
            return new StorageStats(0, 0, false);
        }
    }

    /**
     * Inner class for storage statistics
     */
    public static class StorageStats {
        private final long fileCount;
        private final long totalSize;
        private final boolean available;

        public StorageStats(long fileCount, long totalSize, boolean available) {
            this.fileCount = fileCount;
            this.totalSize = totalSize;
            this.available = available;
        }

        // Getters
        public long getFileCount() {
            return fileCount;
        }

        public long getTotalSize() {
            return totalSize;
        }

        public boolean isAvailable() {
            return available;
        }
    }
}