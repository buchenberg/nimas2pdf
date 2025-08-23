package org.eightfoldconsulting.nimas2pdf.web.controller;

import org.eightfoldconsulting.nimas2pdf.web.entity.ConversionJob;
import org.eightfoldconsulting.nimas2pdf.web.repository.ConversionJobRepository;
import org.eightfoldconsulting.nimas2pdf.web.service.ConversionJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;

/**
 * REST controller for managing conversion jobs.
 */
@RestController
@RequestMapping("/api/conversion-jobs")
@CrossOrigin(origins = "http://localhost:3000")
public class ConversionJobController {

    @Autowired
    private ConversionJobRepository conversionJobRepository;
    
    @Autowired
    private ConversionJobService conversionJobService;

    /**
     * Get all conversion jobs.
     * 
     * @return List of all conversion jobs
     */
    @GetMapping
    public ResponseEntity<List<ConversionJob>> getAllConversionJobs() {
        List<ConversionJob> jobs = conversionJobRepository.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(jobs);
    }

    /**
     * Get conversion jobs by status.
     * 
     * @param status The status to filter by
     * @return List of conversion jobs with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ConversionJob>> getConversionJobsByStatus(@PathVariable String status) {
        try {
            ConversionJob.JobStatus jobStatus = ConversionJob.JobStatus.valueOf(status.toUpperCase());
            List<ConversionJob> jobs = conversionJobRepository.findByStatus(jobStatus);
            return ResponseEntity.ok(jobs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get conversion jobs for a specific package.
     * 
     * @param packageId The ID of the NIMAS package
     * @return List of conversion jobs for the package
     */
    @GetMapping("/package/{packageId}")
    public ResponseEntity<List<ConversionJob>> getConversionJobsByPackage(@PathVariable Long packageId) {
        List<ConversionJob> jobs = conversionJobRepository.findByNimasPackageId(packageId);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Start a new conversion job for a specific package.
     * 
     * @param packageId The ID of the NIMAS package
     * @return The newly created conversion job
     */
    @PostMapping("/start/{packageId}")
    public ResponseEntity<ConversionJob> startConversion(@PathVariable Long packageId) {
        try {
            ConversionJob job = conversionJobService.startConversionJob(packageId);
            return ResponseEntity.ok(job);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get conversion job by ID.
     * 
     * @param jobId The ID of the conversion job
     * @return Conversion job details
     */
    @GetMapping("/{jobId}")
    public ResponseEntity<ConversionJob> getConversionJob(@PathVariable String jobId) {
        Optional<ConversionJob> jobOpt = conversionJobRepository.findByJobId(jobId);
        
        if (jobOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(jobOpt.get());
    }
    




    /**
     * Download the generated PDF for a completed conversion job.
     * 
     * @param jobId The ID of the conversion job
     * @return PDF file as a downloadable resource
     */
    @GetMapping("/{jobId}/download")
    public ResponseEntity<ByteArrayResource> downloadPdf(@PathVariable String jobId) {
        Optional<ConversionJob> jobOpt = conversionJobRepository.findByJobId(jobId);
        
        if (jobOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ConversionJob job = jobOpt.get();
        
        if (job.getStatus() != ConversionJob.JobStatus.COMPLETED || job.getOutputContent() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        ByteArrayResource resource = new ByteArrayResource(job.getOutputContent());
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + job.getOutputFilename() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(job.getOutputContent().length)
                .body(resource);
    }

    /**
     * Cancel a pending or processing conversion job.
     * 
     * @param jobId The ID of the conversion job
     * @return Success response
     */
    @PostMapping("/{jobId}/cancel")
    public ResponseEntity<Boolean> cancelJob(@PathVariable String jobId) {
        try {
            boolean cancelled = conversionJobService.cancelJob(jobId);
            if (cancelled) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.badRequest().body(false);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    /**
     * Retry a failed conversion job.
     * 
     * @param jobId The ID of the conversion job
     * @return Success response
     */
    @PostMapping("/{jobId}/retry")
    public ResponseEntity<ConversionJob> retryJob(@PathVariable String jobId) {
        try {
            ConversionJob job = conversionJobService.retryJob(jobId);
            return ResponseEntity.ok(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
