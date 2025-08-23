package org.eightfoldconsulting.nimas2pdf.web.repository;

import org.eightfoldconsulting.nimas2pdf.web.entity.NimasFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for NimasFile entities.
 */
@Repository
public interface NimasFileRepository extends JpaRepository<NimasFile, Long> {
    
    /**
     * Find files by package ID.
     */
    List<NimasFile> findByNimasPackageId(Long packageId);
    
    /**
     * Find files by package ID and file type.
     */
    List<NimasFile> findByNimasPackageIdAndIsOpfTrue(Long packageId);
    
    /**
     * Find DTBook XML file for a package.
     */
    Optional<NimasFile> findByNimasPackageIdAndIsDtbookTrue(Long packageId);
    
    /**
     * Find image files for a package.
     */
    List<NimasFile> findByNimasPackageIdAndIsImageTrue(Long packageId);
    
    /**
     * Find PDF files for a package.
     */
    List<NimasFile> findByNimasPackageIdAndIsPdfTrue(Long packageId);
    
    /**
     * Find file by package ID and file path.
     */
    Optional<NimasFile> findByNimasPackageIdAndFilePathAndFileName(Long packageId, String filePath, String fileName);
    
    /**
     * Find files by media type.
     */
    List<NimasFile> findByMediaType(String mediaType);
    
    /**
     * Find files by file extension.
     */
    @Query("SELECT f FROM NimasFile f WHERE f.fileName LIKE %:extension")
    List<NimasFile> findByFileExtension(@Param("extension") String extension);
    
    /**
     * Find large files (above specified size).
     */
    List<NimasFile> findByFileSizeGreaterThan(Long size);
    
    /**
     * Count files by package ID.
     */
    long countByNimasPackageId(Long packageId);
    
    /**
     * Find files uploaded after the given date.
     */
    List<NimasFile> findByUploadedAtAfter(java.time.LocalDateTime date);
    
    /**
     * Find text files (OPF, DTBook, etc.) for a package.
     */
    @Query("SELECT f FROM NimasFile f WHERE f.nimasPackage.id = :packageId AND (f.isOpf = true OR f.isDtbook = true OR f.mediaType LIKE 'text/%')")
    List<NimasFile> findTextFilesByPackageId(@Param("packageId") Long packageId);
}
