package org.eightfoldconsulting.nimas2pdf.web.service;

import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionStatus;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of ConversionService that handles NIMAS to PDF conversion.
 */
@Service
public class ConversionServiceImpl implements ConversionService {

    private final Map<String, ConversionStatus> conversions = new ConcurrentHashMap<>();
    private final Path uploadDir = Paths.get("uploads");
    private final Path outputDir = Paths.get("outputs");

    public ConversionServiceImpl() {
        // Create directories if they don't exist
        try {
            Files.createDirectories(uploadDir);
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories", e);
        }
    }

    @Override
    public String startConversion(MultipartFile file, String outputName) throws Exception {
        String conversionId = UUID.randomUUID().toString();
        
        // Save uploaded file
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String savedFilename = conversionId + extension;
        Path savedFilePath = uploadDir.resolve(savedFilename);
        
        file.transferTo(savedFilePath.toFile());
        
        // Create initial status
        ConversionStatus status = new ConversionStatus(conversionId, "PENDING", 0, "File uploaded, starting conversion");
        conversions.put(conversionId, status);
        
        // Start async conversion
        startAsyncConversion(conversionId, savedFilePath, outputName);
        
        return conversionId;
    }

    @Override
    public ConversionStatus getStatus(String conversionId) {
        if (conversionId == null) return null;
        return conversions.get(conversionId);
    }

    @Override
    public Resource getPdfResource(String conversionId) throws Exception {
        if (conversionId == null) return null;
        ConversionStatus status = conversions.get(conversionId);
        if (status == null || !"COMPLETED".equals(status.getStatus())) {
            return null;
        }
        
        String outputFileName = status.getOutputFileName();
        if (outputFileName == null) {
            return null;
        }
        
        Path pdfPath = outputDir.resolve(outputFileName);
        if (!Files.exists(pdfPath)) {
            return null;
        }
        
        return new FileSystemResource(pdfPath.toFile());
    }

    @Override
    public Object getProperties() {
        // TODO: Return properties from existing ApplicationProperties class
        return new Object(); // Placeholder
    }

    @Override
    public void updateProperties(Object properties) {
        // TODO: Update properties in existing ApplicationProperties class
    }

    @Async
    protected void startAsyncConversion(String conversionId, Path inputFile, String outputName) {
        try {
            ConversionStatus status = conversions.get(conversionId);
            status.setStatus("PROCESSING");
            status.setProgress(10);
            status.setLastUpdate(java.time.LocalDateTime.now());
            
            // Generate output filename
            String outputFileName = outputName != null ? outputName : 
                "converted_" + conversionId + ".pdf";
            
            // TODO: Perform conversion using existing XMLConverter class
            // For now, simulate conversion
            simulateConversion(status);
            
            // Update final status
            status.setStatus("COMPLETED");
            status.setProgress(100);
            status.setOutputFileName(outputFileName);
            status.setLastUpdate(java.time.LocalDateTime.now());
            
            // Get file size
            try {
                long fileSize = Files.size(outputDir.resolve(outputFileName));
                status.setFileSize(fileSize);
            } catch (IOException e) {
                // Ignore file size error
            }
            
        } catch (Exception e) {
            ConversionStatus status = conversions.get(conversionId);
            status.setStatus("FAILED");
            status.setMessage("Conversion failed: " + e.getMessage());
            status.setLastUpdate(java.time.LocalDateTime.now());
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private void simulateConversion(ConversionStatus status) {
        try {
            status.setProgress(20);
            status.setMessage("Reading NIMAS file...");
            Thread.sleep(1000);
            
            status.setProgress(40);
            status.setMessage("Processing XML content...");
            Thread.sleep(1000);
            
            status.setProgress(60);
            status.setMessage("Generating PDF...");
            Thread.sleep(1000);
            
            status.setProgress(80);
            status.setMessage("Finalizing PDF...");
            Thread.sleep(1000);
            
            status.setProgress(90);
            status.setMessage("Conversion completed successfully!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            status.setMessage("Conversion interrupted");
        }
    }
}
