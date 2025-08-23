package org.eightfoldconsulting.nimas2pdf.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a PDF conversion job for a NIMAS package.
 */
@Entity
@Table(name = "conversion_jobs")
public class ConversionJob {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_id", unique = true, nullable = false)
    private String jobId; // UUID for external reference
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private NimasPackage nimasPackage;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobStatus status;
    
    @Column(name = "progress")
    private Integer progress; // 0-100
    
    @Column(name = "message")
    private String message; // Current status message
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "error_message")
    private String errorMessage;
    
    @Column(name = "conversion_settings")
    @Lob
    private String conversionSettings; // JSON string of conversion parameters
    
    @Column(name = "output_filename")
    private String outputFilename;
    
    @Column(name = "output_size")
    private Long outputSize;
    
    @Column(name = "output_content")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] outputContent; // Generated PDF content
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public ConversionJob() {}
    
    public ConversionJob(String jobId, NimasPackage nimasPackage) {
        this.jobId = jobId;
        this.nimasPackage = nimasPackage;
        this.status = JobStatus.PENDING;
        this.progress = 0;
        this.message = "Job created";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    
    public NimasPackage getNimasPackage() { return nimasPackage; }
    public void setNimasPackage(NimasPackage nimasPackage) { this.nimasPackage = nimasPackage; }
    
    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { 
        this.status = status; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { 
        this.progress = progress; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { 
        this.message = message; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { 
        this.errorMessage = errorMessage; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getConversionSettings() { return conversionSettings; }
    public void setConversionSettings(String conversionSettings) { this.conversionSettings = conversionSettings; }
    
    public String getOutputFilename() { return outputFilename; }
    public void setOutputFilename(String outputFilename) { this.outputFilename = outputFilename; }
    
    public Long getOutputSize() { return outputSize; }
    public void setOutputSize(Long outputSize) { this.outputSize = outputSize; }
    
    public byte[] getOutputContent() { return outputContent; }
    public void setOutputContent(byte[] outputContent) { 
        this.outputContent = outputContent; 
        this.outputSize = outputContent != null ? (long) outputContent.length : 0L;
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Helper methods
    public void start() {
        this.status = JobStatus.PROCESSING;
        this.startedAt = LocalDateTime.now();
        this.progress = 10;
        this.message = "Conversion started";
        this.updatedAt = LocalDateTime.now();
    }
    
    public void complete(String outputFilename, byte[] outputContent) {
        this.status = JobStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.progress = 100;
        this.message = "Conversion completed successfully";
        this.outputFilename = outputFilename;
        this.outputContent = outputContent;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void fail(String errorMessage) {
        this.status = JobStatus.FAILED;
        this.completedAt = LocalDateTime.now();
        this.errorMessage = errorMessage;
        this.message = "Conversion failed: " + errorMessage;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateProgress(int progress, String message) {
        this.progress = progress;
        this.message = message;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isCompleted() {
        return status == JobStatus.COMPLETED;
    }
    
    public boolean isFailed() {
        return status == JobStatus.FAILED;
    }
    
    public boolean isInProgress() {
        return status == JobStatus.PROCESSING;
    }
    
    public enum JobStatus {
        PENDING,    // Job created, waiting to start
        PROCESSING, // Currently being processed
        COMPLETED,  // Successfully completed
        FAILED,     // Failed with error
        CANCELLED   // Cancelled by user
    }
}
