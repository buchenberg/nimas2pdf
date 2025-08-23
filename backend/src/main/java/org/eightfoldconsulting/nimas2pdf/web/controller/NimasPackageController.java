package org.eightfoldconsulting.nimas2pdf.web.controller;

import org.eightfoldconsulting.nimas2pdf.web.entity.NimasPackage;
import org.eightfoldconsulting.nimas2pdf.web.service.NimasPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

/**
 * Controller for managing NIMAS packages.
 */
@RestController
@RequestMapping("/api/packages")
@CrossOrigin(origins = "*")
public class NimasPackageController {
    
    @Autowired
    private NimasPackageService packageService;
    
    /**
     * Upload a NIMAS ZIP package.
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPackage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }
            
            if (!file.getOriginalFilename().toLowerCase().endsWith(".zip")) {
                return ResponseEntity.badRequest().body("Only ZIP files are allowed");
            }
            
            NimasPackage nimasPackage = packageService.uploadPackage(file);
            
            return ResponseEntity.ok(nimasPackage);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading package: " + e.getMessage());
        }
    }
    
    /**
     * Get all packages.
     */
    @GetMapping
    public ResponseEntity<List<NimasPackage>> getAllPackages() {
        List<NimasPackage> packages = packageService.getAllPackages();
        return ResponseEntity.ok(packages);
    }
    
    /**
     * Get package by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NimasPackage> getPackageById(@PathVariable Long id) {
        Optional<NimasPackage> packageOpt = packageService.getPackageById(id);
        if (packageOpt.isPresent()) {
            return ResponseEntity.ok(packageOpt.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Get package by NIMAS identifier.
     */
    @GetMapping("/nimas/{nimasId}")
    public ResponseEntity<NimasPackage> getPackageByNimasId(@PathVariable String nimasId) {
        Optional<NimasPackage> packageOpt = packageService.getPackageByNimasId(nimasId);
        if (packageOpt.isPresent()) {
            return ResponseEntity.ok(packageOpt.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Search packages by title.
     */
    @GetMapping("/search")
    public ResponseEntity<List<NimasPackage>> searchPackages(@RequestParam String title) {
        List<NimasPackage> packages = packageService.searchPackagesByTitle(title);
        return ResponseEntity.ok(packages);
    }
    
    /**
     * Get packages by status.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<NimasPackage>> getPackagesByStatus(@PathVariable String status) {
        try {
            NimasPackage.PackageStatus packageStatus = NimasPackage.PackageStatus.valueOf(status.toUpperCase());
            List<NimasPackage> packages = packageService.getPackagesByStatus(packageStatus);
            return ResponseEntity.ok(packages);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    /**
     * Get packages ready for conversion.
     */
    @GetMapping("/ready")
    public ResponseEntity<List<NimasPackage>> getPackagesReadyForConversion() {
        List<NimasPackage> packages = packageService.getPackagesReadyForConversion();
        return ResponseEntity.ok(packages);
    }
    
    /**
     * Update package status.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<NimasPackage> updatePackageStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            NimasPackage.PackageStatus packageStatus = NimasPackage.PackageStatus.valueOf(status.toUpperCase());
            NimasPackage updatedPackage = packageService.updatePackageStatus(id, packageStatus);
            return ResponseEntity.ok(updatedPackage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete package.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable Long id) {
        try {
            packageService.deletePackage(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting package: " + e.getMessage());
        }
    }
    
    /**
     * Get package statistics.
     */
    @GetMapping("/statistics")
    public ResponseEntity<NimasPackageService.PackageStatistics> getPackageStatistics() {
        NimasPackageService.PackageStatistics stats = packageService.getPackageStatistics();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Download package as ZIP.
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadPackage(@PathVariable Long id) {
        try {
            Resource resource = packageService.downloadPackageAsZip(id);
            Optional<NimasPackage> packageOpt = packageService.getPackageById(id);
            
            String filename = packageOpt.map(p -> p.getPackageId() + ".zip").orElse("package.zip");
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get package file content.
     */
    @GetMapping("/{id}/files")
    public ResponseEntity<Resource> getPackageFile(
            @PathVariable Long id,
            @RequestParam String filePath,
            @RequestParam String fileName) {
        try {
            Resource resource = packageService.getPackageFileContent(id, filePath, fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Validate package structure.
     */
    @PostMapping("/{id}/validate")
    public ResponseEntity<NimasPackageService.PackageValidationResult> validatePackage(@PathVariable Long id) {
        Optional<NimasPackage> packageOpt = packageService.getPackageById(id);
        if (packageOpt.isPresent()) {
            NimasPackageService.PackageValidationResult validation = packageService.validatePackage(packageOpt.get());
            return ResponseEntity.ok(validation);
        }
        return ResponseEntity.notFound().build();
    }
    
    
}
