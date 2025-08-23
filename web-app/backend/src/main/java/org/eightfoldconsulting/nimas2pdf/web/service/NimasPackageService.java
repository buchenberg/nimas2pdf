package org.eightfoldconsulting.nimas2pdf.web.service;

import org.eightfoldconsulting.nimas2pdf.web.entity.NimasPackage;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

/**
 * Service for managing NIMAS packages.
 */
public interface NimasPackageService {
    
    /**
     * Upload and process a NIMAS ZIP package.
     */
    NimasPackage uploadPackage(MultipartFile zipFile) throws Exception;
    
    /**
     * Get package by ID.
     */
    Optional<NimasPackage> getPackageById(Long id);
    
    /**
     * Get package by NIMAS identifier.
     */
    Optional<NimasPackage> getPackageByNimasId(String nimasId);
    
    /**
     * Get all packages.
     */
    List<NimasPackage> getAllPackages();
    
    /**
     * Search packages by title.
     */
    List<NimasPackage> searchPackagesByTitle(String title);
    
    /**
     * Get packages by status.
     */
    List<NimasPackage> getPackagesByStatus(NimasPackage.PackageStatus status);
    
    /**
     * Get packages ready for conversion.
     */
    List<NimasPackage> getPackagesReadyForConversion();
    
    /**
     * Update package status.
     */
    NimasPackage updatePackageStatus(Long packageId, NimasPackage.PackageStatus status);
    
    /**
     * Delete package and all associated files.
     */
    void deletePackage(Long packageId);
    
    /**
     * Get package statistics.
     */
    PackageStatistics getPackageStatistics();
    
    /**
     * Validate NIMAS package structure.
     */
    PackageValidationResult validatePackage(NimasPackage nimasPackage);
    
    /**
     * Extract package metadata from OPF file.
     */
    void extractPackageMetadata(NimasPackage nimasPackage);
    
    /**
     * Download package as ZIP file.
     */
    Resource downloadPackageAsZip(Long packageId) throws Exception;
    
    /**
     * Get package file content.
     */
    Resource getPackageFileContent(Long packageId, String filePath, String fileName) throws Exception;
    
    /**
     * Statistics for NIMAS packages.
     */
    class PackageStatistics {
        private long totalPackages;
        private long uploadedPackages;
        private long readyPackages;
        private long processingPackages;
        private long errorPackages;
        
        // Constructors, getters, setters
        public PackageStatistics() {}
        
        public PackageStatistics(long total, long uploaded, long ready, long processing, long error) {
            this.totalPackages = total;
            this.uploadedPackages = uploaded;
            this.readyPackages = ready;
            this.processingPackages = processing;
            this.errorPackages = error;
        }
        
        // Getters and setters
        public long getTotalPackages() { return totalPackages; }
        public void setTotalPackages(long totalPackages) { this.totalPackages = totalPackages; }
        
        public long getUploadedPackages() { return uploadedPackages; }
        public void setUploadedPackages(long uploadedPackages) { this.uploadedPackages = uploadedPackages; }
        
        public long getReadyPackages() { return readyPackages; }
        public void setReadyPackages(long readyPackages) { this.readyPackages = readyPackages; }
        
        public long getProcessingPackages() { return processingPackages; }
        public void setProcessingPackages(long processingPackages) { this.processingPackages = processingPackages; }
        
        public long getErrorPackages() { return errorPackages; }
        public void setErrorPackages(long errorPackages) { this.errorPackages = errorPackages; }
    }
    
    /**
     * Validation result for NIMAS packages.
     */
    class PackageValidationResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;
        
        public PackageValidationResult() {
            this.valid = true;
            this.errors = new ArrayList<>();
            this.warnings = new ArrayList<>();
        }
        
        public PackageValidationResult(boolean valid, List<String> errors, List<String> warnings) {
            this.valid = valid;
            this.errors = errors != null ? errors : new ArrayList<>();
            this.warnings = warnings != null ? warnings : new ArrayList<>();
        }
        
        // Getters and setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
        
        public void addError(String error) {
            this.errors.add(error);
            this.valid = false;
        }
        
        public void addWarning(String warning) {
            this.warnings.add(warning);
        }
    }
}
