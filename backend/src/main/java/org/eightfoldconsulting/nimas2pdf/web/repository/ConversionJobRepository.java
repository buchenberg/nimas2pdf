package org.eightfoldconsulting.nimas2pdf.web.repository;

import org.eightfoldconsulting.nimas2pdf.web.entity.ConversionJob;
import org.eightfoldconsulting.nimas2pdf.web.dto.ConversionJobSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for ConversionJob entities.
 */
@Repository
public interface ConversionJobRepository extends JpaRepository<ConversionJob, Long> {
    
    /**
     * Find job by external job ID.
     */
    Optional<ConversionJob> findByJobId(String jobId);
    
    /**
     * Find jobs by package ID.
     */
    List<ConversionJob> findByNimasPackageId(Long packageId);
    
    /**
     * Find jobs by status.
     */
    List<ConversionJob> findByStatus(ConversionJob.JobStatus status);
    
    /**
     * Find jobs by status and package ID.
     */
    List<ConversionJob> findByStatusAndNimasPackageId(ConversionJob.JobStatus status, Long packageId);
    
    /**
     * Find pending jobs.
     */
    List<ConversionJob> findByStatusOrderByCreatedAtAsc(ConversionJob.JobStatus status);
    
    /**
     * Find jobs created after the given date.
     */
    List<ConversionJob> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Find jobs completed after the given date.
     */
    List<ConversionJob> findByCompletedAtAfter(LocalDateTime date);
    
    /**
     * Find failed jobs.
     */
    List<ConversionJob> findByStatusOrderByCreatedAtDesc(ConversionJob.JobStatus status);
    
    /**
     * Find jobs with errors.
     */
    @Query("SELECT j FROM ConversionJob j WHERE j.errorMessage IS NOT NULL AND j.errorMessage != ''")
    List<ConversionJob> findJobsWithErrors();
    
    /**
     * Find all jobs ordered by creation date (newest first).
     * This method excludes LOB fields to avoid PostgreSQL auto-commit issues.
     */
    @Query(value = "SELECT j.id, j.job_id, j.status, j.progress, j.message, j.started_at, j.updated_at, j.created_at, j.completed_at, j.error_message, j.conversion_settings, j.output_filename, j.output_size, j.package_id FROM conversion_jobs j ORDER BY j.created_at DESC", nativeQuery = true)
    List<Object[]> findAllJobsSummaryOrderByCreatedAtDesc();
    
    /**
     * Find jobs by progress range.
     */
    @Query("SELECT j FROM ConversionJob j WHERE j.progress BETWEEN :minProgress AND :maxProgress")
    List<ConversionJob> findByProgressRange(@Param("minProgress") int minProgress, @Param("maxProgress") int maxProgress);
    
    /**
     * Count jobs by status.
     */
    long countByStatus(ConversionJob.JobStatus status);
    
    /**
     * Count jobs by package ID.
     */
    long countByNimasPackageId(Long packageId);
    
    /**
     * Find recent jobs for a package.
     */
    @Query("SELECT j FROM ConversionJob j WHERE j.nimasPackage.id = :packageId ORDER BY j.createdAt DESC")
    List<ConversionJob> findRecentJobsByPackageId(@Param("packageId") Long packageId);
    
    /**
     * Find jobs that need to be processed (pending or failed).
     */
    @Query("SELECT j FROM ConversionJob j WHERE j.status IN ('PENDING', 'FAILED') ORDER BY j.createdAt ASC")
    List<ConversionJob> findJobsNeedingProcessing();
    
    /**
     * Find completed jobs with output.
     */
    @Query("SELECT j FROM ConversionJob j WHERE j.status = 'COMPLETED' AND j.outputContent IS NOT NULL")
    List<ConversionJob> findCompletedJobsWithOutput();
}
