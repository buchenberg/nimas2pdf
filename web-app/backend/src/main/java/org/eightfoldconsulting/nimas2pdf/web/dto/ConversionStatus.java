package org.eightfoldconsulting.nimas2pdf.web.dto;

import java.time.LocalDateTime;

/**
 * DTO for conversion status information.
 */
public class ConversionStatus {
    private String conversionId;
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED
    private int progress; // 0-100
    private String message;
    private LocalDateTime startTime;
    private LocalDateTime lastUpdate;
    private String outputFileName;
    private long fileSize;

    public ConversionStatus() {}

    public ConversionStatus(String conversionId, String status, int progress, String message) {
        this.conversionId = conversionId;
        this.status = status;
        this.progress = progress;
        this.message = message;
        this.startTime = LocalDateTime.now();
        this.lastUpdate = LocalDateTime.now();
    }

    // Getters and Setters
    public String getConversionId() {
        return conversionId;
    }

    public void setConversionId(String conversionId) {
        this.conversionId = conversionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
