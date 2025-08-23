package org.eightfoldconsulting.nimas2pdf.web.service;

import org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties;
import org.eightfoldconsulting.nimas2pdf.web.entity.ConversionJob;
import org.eightfoldconsulting.nimas2pdf.web.entity.NimasFile;
import org.eightfoldconsulting.nimas2pdf.web.entity.NimasPackage;
import org.eightfoldconsulting.nimas2pdf.web.repository.ConversionJobRepository;
import org.eightfoldconsulting.nimas2pdf.web.repository.NimasPackageRepository;
import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionJobSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Service for managing NIMAS to PDF conversion jobs.
 */
@Service
public class ConversionJobService {

    @Autowired
    private ConversionJobRepository conversionJobRepository;
    
    @Autowired
    private NimasPackageRepository packageRepository;
    
    @Autowired
    private XMLConverter xmlConverter;

    /**
     * Start a new conversion job for a NIMAS package.
     */
    @Transactional
    public ConversionJob startConversionJob(Long packageId) throws Exception {
        NimasPackage nimasPackage = packageRepository.findById(packageId)
            .orElseThrow(() -> new IllegalArgumentException("Package not found: " + packageId));
        
        // Find the main content XML file and get its content within the transaction
        NimasFile mainContentFile = findMainContentFile(nimasPackage);
        if (mainContentFile == null) {
            throw new Exception("No main content XML file found in package");
        }
        
        // Get the file content within the transaction
        byte[] fileContent = mainContentFile.getContent();
        if (fileContent == null || fileContent.length == 0) {
            throw new Exception("File content is empty or null");
        }
        
        // Create new conversion job
        String jobId = UUID.randomUUID().toString();
        ConversionJob job = new ConversionJob(jobId, nimasPackage);
        job.setStatus(ConversionJob.JobStatus.PENDING);
        job.setMessage("Starting conversion...");
        job.setProgress(0);
        
        // Save initial job
        job = conversionJobRepository.save(job);
        
        // Start async conversion with the file content already loaded
        startAsyncConversion(job, fileContent, mainContentFile.getFileName());
        
        return job;
    }

    /**
     * Start the actual conversion process asynchronously.
     */
    @Async
    @Transactional
    protected void startAsyncConversion(ConversionJob job, byte[] fileContent, String fileName) {
        try {
            // Update status to processing
            job.setStatus(ConversionJob.JobStatus.PROCESSING);
            job.setStartedAt(LocalDateTime.now());
            job.setMessage("Processing NIMAS package...");
            job.setProgress(10);
            conversionJobRepository.save(job);
            
            // Create temporary files for conversion
            Path tempDir = Files.createTempDirectory("nimas2pdf_" + job.getJobId());
            Path xmlFile = tempDir.resolve(fileName);
            Path outputFile = tempDir.resolve(fileName.replace(".xml", ".pdf"));
            
            // Write XML content to temp file
            Files.write(xmlFile, fileContent);
            
            job.setMessage("Starting XML to PDF conversion...");
            job.setProgress(30);
            conversionJobRepository.save(job);
            
            // Perform the actual conversion
            xmlConverter.convertToPdf(xmlFile, outputFile, null, null);
            
            job.setMessage("PDF generation completed, processing output...");
            job.setProgress(80);
            conversionJobRepository.save(job);
            
            // Read the generated PDF
            byte[] pdfContent = Files.readAllBytes(outputFile);
            job.setOutputContent(pdfContent);
            job.setOutputFilename(outputFile.getFileName().toString());
            job.setOutputSize((long) pdfContent.length);
            
            // Clean up temp files
            try {
                Files.deleteIfExists(xmlFile);
                Files.deleteIfExists(outputFile);
                Files.deleteIfExists(tempDir);
            } catch (IOException e) {
                // Log warning but don't fail the conversion
                System.err.println("Warning: Could not delete temporary files: " + e.getMessage());
            }
            
            // Mark as completed
            job.setStatus(ConversionJob.JobStatus.COMPLETED);
            job.setCompletedAt(LocalDateTime.now());
            job.setProgress(100);
            job.setMessage("Conversion completed successfully");
            conversionJobRepository.save(job);
            
        } catch (Exception e) {
            // Handle conversion failure
            job.setStatus(ConversionJob.JobStatus.FAILED);
            job.setErrorMessage("Conversion failed: " + e.getMessage());
            job.setMessage("Conversion failed");
            conversionJobRepository.save(job);
            
            // Log the error
            System.err.println("Conversion failed for job " + job.getJobId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Find the main content XML file in the NIMAS package.
     * This looks for files that are likely the main content (not OPF, not images, etc.)
     */
    private NimasFile findMainContentFile(NimasPackage nimasPackage) {
        List<NimasFile> files = nimasPackage.getFiles();
        
        // Look for common content file names
        for (NimasFile file : files) {
            String filename = file.getFileName().toLowerCase();
            if (filename.endsWith(".xml") && 
                !filename.endsWith(".opf") && 
                !filename.contains("toc") &&
                !filename.contains("nav")) {
                return file;
            }
        }
        
        // If no obvious content file found, return the first XML file that's not OPF
        for (NimasFile file : files) {
            if (file.getFileName().toLowerCase().endsWith(".xml") && 
                !file.getFileName().toLowerCase().endsWith(".opf")) {
                return file;
            }
        }
        
        return null;
    }

    /**
     * Get all conversion jobs ordered by creation date (newest first).
     * Returns summary DTOs to avoid LOB field access issues.
     */
    public List<ConversionJobSummary> getAllConversionJobs() {
        List<Object[]> results = conversionJobRepository.findAllJobsSummaryOrderByCreatedAtDesc();
        return results.stream()
            .map(this::mapToConversionJobSummary)
            .toList();
    }
    
    /**
     * Map database result row to ConversionJobSummary.
     */
    private ConversionJobSummary mapToConversionJobSummary(Object[] row) {
        ConversionJobSummary summary = new ConversionJobSummary();
        
        // Map the Object[] to ConversionJobSummary fields
        // Order: id, job_id, status, progress, message, started_at, updated_at, created_at, completed_at, error_message, conversion_settings, output_filename, output_size, package_id
        summary.setId((Long) row[0]);
        summary.setJobId((String) row[1]);
        summary.setStatus(ConversionJob.JobStatus.valueOf((String) row[2]));
        summary.setProgress((Integer) row[3]);
        summary.setMessage((String) row[4]);
        summary.setStartedAt(convertTimestampToLocalDateTime(row[5]));
        summary.setUpdatedAt(convertTimestampToLocalDateTime(row[6]));
        summary.setCreatedAt(convertTimestampToLocalDateTime(row[7]));
        summary.setCompletedAt(convertTimestampToLocalDateTime(row[8]));
        summary.setErrorMessage((String) row[9]);
        summary.setConversionSettings((String) row[10]);
        summary.setOutputFilename((String) row[11]);
        summary.setOutputSize((Long) row[12]);
        
        // For the package, we'll need to fetch it separately if needed
        // For now, we'll set it to null to avoid LOB issues
        summary.setNimasPackage(null);
        
        return summary;
    }
    
    /**
     * Convert java.sql.Timestamp to LocalDateTime, handling null values.
     */
    private LocalDateTime convertTimestampToLocalDateTime(Object timestamp) {
        if (timestamp == null) {
            return null;
        }
        if (timestamp instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) timestamp).toLocalDateTime();
        }
        if (timestamp instanceof LocalDateTime) {
            return (LocalDateTime) timestamp;
        }
        throw new IllegalArgumentException("Cannot convert " + timestamp.getClass() + " to LocalDateTime");
    }

    /**
     * Get conversion job by ID.
     */
    public ConversionJob getConversionJob(String jobId) {
        return conversionJobRepository.findByJobId(jobId).orElse(null);
    }

    /**
     * Get all conversion jobs for a package.
     * Returns summary DTOs to avoid LOB field access issues.
     */
    public List<ConversionJobSummary> getConversionJobsForPackage(Long packageId) {
        List<ConversionJob> fullJobs = conversionJobRepository.findByNimasPackageId(packageId);
        return fullJobs.stream()
            .map(ConversionJobSummary::new)
            .toList();
    }
    
    /**
     * Get conversion jobs by status.
     * Returns summary DTOs to avoid LOB field access issues.
     */
    public List<ConversionJobSummary> getConversionJobsByStatus(ConversionJob.JobStatus status) {
        List<ConversionJob> fullJobs = conversionJobRepository.findByStatus(status);
        return fullJobs.stream()
            .map(ConversionJobSummary::new)
            .toList();
    }

    /**
     * Cancel a pending or processing job.
     */
    public boolean cancelJob(String jobId) {
        ConversionJob job = conversionJobRepository.findByJobId(jobId).orElse(null);
        if (job != null && (job.getStatus() == ConversionJob.JobStatus.PENDING || 
                           job.getStatus() == ConversionJob.JobStatus.PROCESSING)) {
            job.setStatus(ConversionJob.JobStatus.CANCELLED);
            job.setMessage("Job cancelled by user");
            conversionJobRepository.save(job);
            return true;
        }
        return false;
    }

    /**
     * Retry a failed job.
     */
    public ConversionJob retryJob(String jobId) throws Exception {
        ConversionJob originalJob = conversionJobRepository.findByJobId(jobId).orElse(null);
        if (originalJob != null && originalJob.getStatus() == ConversionJob.JobStatus.FAILED) {
            // Create a new job based on the failed one
            return startConversionJob(originalJob.getNimasPackage().getId());
        }
        throw new IllegalArgumentException("Cannot retry job: " + jobId);
    }
    
    /**
     * Get conversion job with output content for download.
     * This method is transactional to allow LOB field access.
     */
    @Transactional
    public ConversionJob getConversionJobForDownload(String jobId) {
        return conversionJobRepository.findByJobId(jobId).orElse(null);
    }
}
