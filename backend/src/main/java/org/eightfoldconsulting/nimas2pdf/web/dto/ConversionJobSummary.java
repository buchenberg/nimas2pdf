package org.eightfoldconsulting.nimas2pdf.web.dto;

import org.eightfoldconsulting.nimas2pdf.web.entity.ConversionJob;
import org.eightfoldconsulting.nimas2pdf.web.entity.NimasPackage;

import java.time.LocalDateTime;

/**
 * DTO for ConversionJob summary information, excluding LOB fields.
 * This is used to avoid PostgreSQL auto-commit issues when listing conversion jobs.
 */
public class ConversionJobSummary {
    
    private Long id;
    private String jobId;
    private ConversionJob.JobStatus status;
    private Integer progress;
    private String message;
    private LocalDateTime startedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String errorMessage;
    private String conversionSettings;
    private String outputFilename;
    private Long outputSize;
    private NimasPackage nimasPackage;
    
    // Constructors
    public ConversionJobSummary() {}
    
    public ConversionJobSummary(ConversionJob job) {
        this.id = job.getId();
        this.jobId = job.getJobId();
        this.status = job.getStatus();
        this.progress = job.getProgress();
        this.message = job.getMessage();
        this.startedAt = job.getStartedAt();
        this.updatedAt = job.getUpdatedAt();
        this.createdAt = job.getCreatedAt();
        this.completedAt = job.getCompletedAt();
        this.errorMessage = job.getErrorMessage();
        this.conversionSettings = job.getConversionSettings();
        this.outputFilename = job.getOutputFilename();
        this.outputSize = job.getOutputSize();
        this.nimasPackage = job.getNimasPackage();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    
    public ConversionJob.JobStatus getStatus() { return status; }
    public void setStatus(ConversionJob.JobStatus status) { this.status = status; }
    
    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public String getConversionSettings() { return conversionSettings; }
    public void setConversionSettings(String conversionSettings) { this.conversionSettings = conversionSettings; }
    
    public String getOutputFilename() { return outputFilename; }
    public void setOutputFilename(String outputFilename) { this.outputFilename = outputFilename; }
    
    public Long getOutputSize() { return outputSize; }
    public void setOutputSize(Long outputSize) { this.outputSize = outputSize; }
    
    public NimasPackage getNimasPackage() { return nimasPackage; }
    public void setNimasPackage(NimasPackage nimasPackage) { this.nimasPackage = nimasPackage; }
    
    /**
     * Get the package ID for JSON serialization.
     * This avoids exposing the full NimasPackage object.
     */
    public Long getPackageId() { 
        return nimasPackage != null ? nimasPackage.getId() : null; 
    }
    
    /**
     * Check if the job is completed.
     */
    public boolean isCompleted() {
        return status == ConversionJob.JobStatus.COMPLETED;
    }
    
    /**
     * Check if the job failed.
     */
    public boolean isFailed() {
        return status == ConversionJob.JobStatus.FAILED;
    }
    
    /**
     * Check if the job is in progress.
     */
    public boolean isInProgress() {
        return status == ConversionJob.JobStatus.PROCESSING;
    }
    
    /**
     * Check if the job is pending.
     */
    public boolean isPending() {
        return status == ConversionJob.JobStatus.PENDING;
    }
}
