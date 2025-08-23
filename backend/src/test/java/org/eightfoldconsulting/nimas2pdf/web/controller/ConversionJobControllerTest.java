package org.eightfoldconsulting.nimas2pdf.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eightfoldconsulting.nimas2pdf.web.entity.ConversionJob;
import org.eightfoldconsulting.nimas2pdf.web.entity.NimasPackage;
import org.eightfoldconsulting.nimas2pdf.web.repository.ConversionJobRepository;
import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionJobSummary;
import org.eightfoldconsulting.nimas2pdf.web.service.ConversionJobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for ConversionJobController.
 */
@WebMvcTest(ConversionJobController.class)
class ConversionJobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversionJobRepository conversionJobRepository;
    
    @MockBean
    private ConversionJobService conversionJobService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllConversionJobs() throws Exception {
        // Arrange
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        ConversionJob job1 = new ConversionJob("job-1", nimasPackage);
        ConversionJob job2 = new ConversionJob("job-2", nimasPackage);
        
        ConversionJobSummary summary1 = new ConversionJobSummary(job1);
        ConversionJobSummary summary2 = new ConversionJobSummary(job2);
        
        when(conversionJobService.getAllConversionJobs())
                .thenReturn(Arrays.asList(summary1, summary2));

        // Act & Assert
        mockMvc.perform(get("/api/conversion-jobs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].jobId").value("job-1"))
                .andExpect(jsonPath("$[1].jobId").value("job-2"));
    }

    @Test
    void testGetConversionJobById() throws Exception {
        // Arrange
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        ConversionJob job = new ConversionJob("job-1", nimasPackage);
        
        when(conversionJobRepository.findByJobId("job-1"))
                .thenReturn(Optional.of(job));

        // Act & Assert
        mockMvc.perform(get("/api/conversion-jobs/job-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.jobId").value("job-1"));
    }

    @Test
    void testGetConversionJobById_NotFound() throws Exception {
        // Arrange
        when(conversionJobRepository.findByJobId("nonexistent"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/conversion-jobs/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetConversionJobsByStatus() throws Exception {
        // Arrange
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        ConversionJob job = new ConversionJob("job-1", nimasPackage);
        ConversionJobSummary summary = new ConversionJobSummary(job);
        
        when(conversionJobService.getConversionJobsByStatus(ConversionJob.JobStatus.COMPLETED))
                .thenReturn(Arrays.asList(summary));

        // Act & Assert
        mockMvc.perform(get("/api/conversion-jobs/status/completed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].jobId").value("job-1"));
    }

    @Test
    void testGetConversionJobsByStatus_InvalidStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/conversion-jobs/status/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetConversionJobsByPackage() throws Exception {
        // Arrange
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        ConversionJob job = new ConversionJob("job-1", nimasPackage);
        ConversionJobSummary summary = new ConversionJobSummary(job);
        
        when(conversionJobService.getConversionJobsForPackage(1L))
                .thenReturn(Arrays.asList(summary));

        // Act & Assert
        mockMvc.perform(get("/api/conversion-jobs/package/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].jobId").value("job-1"));
    }

    @Test
    void testDownloadPdf_Success() throws Exception {
        // Arrange
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        ConversionJob job = new ConversionJob("job-1", nimasPackage);
        job.complete("test.pdf", "PDF content".getBytes());
        
        when(conversionJobRepository.findByJobId("job-1"))
                .thenReturn(Optional.of(job));

        // Act & Assert
        mockMvc.perform(get("/api/conversion-jobs/job-1/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.pdf\""))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void testDownloadPdf_JobNotFound() throws Exception {
        // Arrange
        when(conversionJobRepository.findByJobId("nonexistent"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/conversion-jobs/nonexistent/download"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDownloadPdf_JobNotCompleted() throws Exception {
        // Arrange
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        ConversionJob job = new ConversionJob("job-1", nimasPackage);
        // Job is still pending, not completed
        
        when(conversionJobRepository.findByJobId("job-1"))
                .thenReturn(Optional.of(job));

        // Act & Assert
        mockMvc.perform(get("/api/conversion-jobs/job-1/download"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCancelConversionJob_Success() throws Exception {
        // Arrange
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        ConversionJob job = new ConversionJob("job-1", nimasPackage);
        // Job is pending, can be cancelled
        
        when(conversionJobRepository.findByJobId("job-1"))
                .thenReturn(Optional.of(job));
        when(conversionJobRepository.save(any(ConversionJob.class)))
                .thenReturn(job);

        // Act & Assert
        mockMvc.perform(post("/api/conversion-jobs/job-1/cancel"))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelConversionJob_AlreadyCompleted() throws Exception {
        // Arrange
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        ConversionJob job = new ConversionJob("job-1", nimasPackage);
        job.complete("test.pdf", "PDF content".getBytes());
        // Job is completed, cannot be cancelled
        
        when(conversionJobRepository.findByJobId("job-1"))
                .thenReturn(Optional.of(job));

        // Act & Assert
        mockMvc.perform(post("/api/conversion-jobs/job-1/cancel"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Job cannot be cancelled in its current state"));
    }

    @Test
    void testRetryConversionJob_Success() throws Exception {
        // Arrange
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        ConversionJob job = new ConversionJob("job-1", nimasPackage);
        job.fail("Test error");
        // Job is failed, can be retried
        
        when(conversionJobRepository.findByJobId("job-1"))
                .thenReturn(Optional.of(job));
        when(conversionJobRepository.save(any(ConversionJob.class)))
                .thenReturn(job);

        // Act & Assert
        mockMvc.perform(post("/api/conversion-jobs/job-1/retry"))
                .andExpect(status().isOk());
    }

    @Test
    void testRetryConversionJob_NotFailed() throws Exception {
        // Arrange
        NimasPackage nimasPackage = new NimasPackage("test-123", "Test Package");
        ConversionJob job = new ConversionJob("job-1", nimasPackage);
        // Job is pending, not failed, cannot be retried
        
        when(conversionJobRepository.findByJobId("job-1"))
                .thenReturn(Optional.of(job));

        // Act & Assert
        mockMvc.perform(post("/api/conversion-jobs/job-1/retry"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Only failed jobs can be retried"));
    }
}
