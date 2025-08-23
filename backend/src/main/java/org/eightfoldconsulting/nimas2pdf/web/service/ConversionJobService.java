package org.eightfoldconsulting.nimas2pdf.web.service;

import org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties;
import org.eightfoldconsulting.nimas2pdf.web.entity.ConversionJob;
import org.eightfoldconsulting.nimas2pdf.web.entity.NimasFile;
import org.eightfoldconsulting.nimas2pdf.web.entity.NimasPackage;
import org.eightfoldconsulting.nimas2pdf.web.repository.ConversionJobRepository;
import org.eightfoldconsulting.nimas2pdf.web.repository.NimasPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
    
    // Temporarily comment out to isolate the issue
    // @Autowired
    // private XMLConverter xmlConverter;

    /**
     * Start a new conversion job for a NIMAS package.
     */
    public ConversionJob startConversionJob(Long packageId) throws Exception {
        NimasPackage nimasPackage = packageRepository.findById(packageId)
            .orElseThrow(() -> new IllegalArgumentException("Package not found: " + packageId));
        
        // Create new conversion job
        String jobId = UUID.randomUUID().toString();
        ConversionJob job = new ConversionJob(jobId, nimasPackage);
        job.setStatus(ConversionJob.JobStatus.PENDING);
        job.setMessage("Starting conversion...");
        job.setProgress(0);
        
        // Save initial job
        job = conversionJobRepository.save(job);
        
        // Start async conversion
        startAsyncConversion(job);
        
        return job;
    }

    /**
     * Start the actual conversion process asynchronously.
     */
    @Async
    protected void startAsyncConversion(ConversionJob job) {
        try {
            // Update status to processing
            job.setStatus(ConversionJob.JobStatus.PROCESSING);
            job.setStartedAt(LocalDateTime.now());
            job.setMessage("Processing NIMAS package...");
            job.setProgress(10);
            conversionJobRepository.save(job);
            
            // For now, just simulate the conversion process
            job.setMessage("Simulating conversion process...");
            job.setProgress(50);
            conversionJobRepository.save(job);
            
            // Simulate completion
            job.setStatus(ConversionJob.JobStatus.COMPLETED);
            job.setCompletedAt(LocalDateTime.now());
            job.setProgress(100);
            job.setMessage("Conversion completed successfully (simulated)");
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
     * Get conversion job by ID.
     */
    public ConversionJob getConversionJob(String jobId) {
        return conversionJobRepository.findByJobId(jobId).orElse(null);
    }

    /**
     * Get all conversion jobs for a package.
     */
    public List<ConversionJob> getConversionJobsForPackage(Long packageId) {
        return conversionJobRepository.findByNimasPackageId(packageId);
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
}
