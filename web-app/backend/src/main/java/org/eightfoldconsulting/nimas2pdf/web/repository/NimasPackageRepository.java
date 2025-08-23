package org.eightfoldconsulting.nimas2pdf.web.repository;

import org.eightfoldconsulting.nimas2pdf.web.entity.NimasPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for NimasPackage entities.
 */
@Repository
public interface NimasPackageRepository extends JpaRepository<NimasPackage, Long> {
    
    /**
     * Find package by NIMAS identifier.
     */
    Optional<NimasPackage> findByPackageId(String packageId);
    
    /**
     * Find packages by title containing the given text.
     */
    List<NimasPackage> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Find packages by creator.
     */
    List<NimasPackage> findByCreatorContainingIgnoreCase(String creator);
    
    /**
     * Find packages by subject.
     */
    List<NimasPackage> findBySubjectContainingIgnoreCase(String subject);
    
    /**
     * Find packages by language.
     */
    List<NimasPackage> findByLanguage(String language);
    
    /**
     * Find packages by status.
     */
    List<NimasPackage> findByStatus(NimasPackage.PackageStatus status);
    
    /**
     * Find packages uploaded after the given date.
     */
    List<NimasPackage> findByUploadedAtAfter(java.time.LocalDateTime date);
    
    /**
     * Find packages by source (ISBN).
     */
    Optional<NimasPackage> findBySource(String source);
    
    /**
     * Count packages by status.
     */
    long countByStatus(NimasPackage.PackageStatus status);
    
    /**
     * Find packages with conversion jobs.
     */
    @Query("SELECT DISTINCT p FROM NimasPackage p JOIN p.conversionJobs j WHERE j.status = :jobStatus")
    List<NimasPackage> findPackagesWithConversionJobsByStatus(@Param("jobStatus") String jobStatus);
    
    /**
     * Find packages ready for conversion.
     */
    @Query("SELECT p FROM NimasPackage p WHERE p.status = 'READY' AND NOT EXISTS (SELECT j FROM ConversionJob j WHERE j.nimasPackage = p AND j.status IN ('PENDING', 'PROCESSING'))")
    List<NimasPackage> findPackagesReadyForConversion();
}
