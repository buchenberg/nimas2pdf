package org.eightfoldconsulting.nimas2pdf.web.controller;

import org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties;
import org.eightfoldconsulting.nimas2pdf.web.entity.NimasPackage;
import org.eightfoldconsulting.nimas2pdf.web.repository.NimasPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for managing conversion properties of NIMAS packages.
 */
@RestController
@RequestMapping("/api/packages/{packageId}/properties")
@CrossOrigin(origins = "http://localhost:3000")
public class ConversionPropertiesController {

    @Autowired
    private NimasPackageRepository packageRepository;

    /**
     * Get conversion properties for a specific package.
     * 
     * @param packageId The ID of the NIMAS package
     * @return ConversionProperties or default properties if none exist
     */
    @GetMapping
    public ResponseEntity<ConversionProperties> getConversionProperties(@PathVariable Long packageId) {
        Optional<NimasPackage> packageOpt = packageRepository.findById(packageId);
        
        if (packageOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        NimasPackage nimasPackage = packageOpt.get();
        ConversionProperties properties = nimasPackage.getConversionProperties();
        
        // If no properties exist, return default properties
        if (properties == null) {
            properties = new ConversionProperties();
        }
        
        return ResponseEntity.ok(properties);
    }

    /**
     * Update conversion properties for a specific package.
     * 
     * @param packageId The ID of the NIMAS package
     * @param properties The new conversion properties
     * @return Updated ConversionProperties
     */
    @PutMapping
    public ResponseEntity<ConversionProperties> updateConversionProperties(
            @PathVariable Long packageId,
            @RequestBody ConversionProperties properties) {
        
        Optional<NimasPackage> packageOpt = packageRepository.findById(packageId);
        
        if (packageOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        NimasPackage nimasPackage = packageOpt.get();
        nimasPackage.setConversionProperties(properties);
        
        // Save the updated package
        NimasPackage savedPackage = packageRepository.save(nimasPackage);
        
        return ResponseEntity.ok(savedPackage.getConversionProperties());
    }

    /**
     * Reset conversion properties to defaults for a specific package.
     * 
     * @param packageId The ID of the NIMAS package
     * @return Default ConversionProperties
     */
    @PostMapping("/reset")
    public ResponseEntity<ConversionProperties> resetConversionProperties(@PathVariable Long packageId) {
        Optional<NimasPackage> packageOpt = packageRepository.findById(packageId);
        
        if (packageOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        NimasPackage nimasPackage = packageOpt.get();
        ConversionProperties defaultProperties = new ConversionProperties();
        nimasPackage.setConversionProperties(defaultProperties);
        
        // Save the updated package
        NimasPackage savedPackage = packageRepository.save(nimasPackage);
        
        return ResponseEntity.ok(savedPackage.getConversionProperties());
    }
}
