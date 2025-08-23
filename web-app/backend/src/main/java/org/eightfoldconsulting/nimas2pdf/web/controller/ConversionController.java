package org.eightfoldconsulting.nimas2pdf.web.controller;

import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionRequest;
import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionResponse;
import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionStatus;
import org.eightfoldconsulting.nimas2pdf.web.service.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;

/**
 * REST controller for NIMAS to PDF conversion operations.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Configure appropriately for production
public class ConversionController {

    @Autowired
    private ConversionService conversionService;

    /**
     * Upload and convert a NIMAS file to PDF.
     */
    @PostMapping("/convert")
    public ResponseEntity<ConversionResponse> convertNimas(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "outputName", required = false) String outputName) {
        
        try {
            String conversionId = conversionService.startConversion(file, outputName);
            return ResponseEntity.ok(new ConversionResponse(conversionId, "Conversion started"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ConversionResponse(null, "Error: " + e.getMessage()));
        }
    }

    /**
     * Get the status of a conversion.
     */
    @GetMapping("/status/{conversionId}")
    public ResponseEntity<ConversionStatus> getStatus(@PathVariable String conversionId) {
        ConversionStatus status = conversionService.getStatus(conversionId);
        if (status != null) {
            return ResponseEntity.ok(status);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Download the converted PDF.
     */
    @GetMapping("/download/{conversionId}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String conversionId) {
        try {
            Resource pdfResource = conversionService.getPdfResource(conversionId);
            if (pdfResource != null && pdfResource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + pdfResource.getFilename() + "\"")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(pdfResource);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get conversion properties/configuration.
     */
    @GetMapping("/properties")
    public ResponseEntity<?> getProperties() {
        return ResponseEntity.ok(conversionService.getProperties());
    }

    /**
     * Update conversion properties/configuration.
     */
    @PostMapping("/properties")
    public ResponseEntity<?> updateProperties(@Valid @RequestBody Object properties) {
        conversionService.updateProperties(properties);
        return ResponseEntity.ok().build();
    }
}
