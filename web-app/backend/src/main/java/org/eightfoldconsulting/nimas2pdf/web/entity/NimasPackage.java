package org.eightfoldconsulting.nimas2pdf.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.eightfoldconsulting.nimas2pdf.web.config.ConversionProperties;

/**
 * Entity representing a NIMAS package (OPF file).
 */
@Entity
@Table(name = "nimas_packages")
public class NimasPackage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "package_id", unique = true, nullable = false)
    private String packageId; // NIMAS identifier from OPF
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "creator")
    private String creator;
    
    @Column(name = "language")
    private String language;
    
    @Column(name = "publisher")
    private String publisher;
    
    @Column(name = "format")
    private String format; // NIMAS version
    
    @Column(name = "source")
    private String source; // ISBN or other source identifier
    
    @Column(name = "subject")
    private String subject;
    
    @Column(name = "audience_level")
    private String audienceLevel;
    
    @Column(name = "source_edition")
    private String sourceEdition;
    
    @Column(name = "source_date")
    private String sourceDate;
    
    @Column(name = "copyright_date")
    private String copyrightDate;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "version")
    private String version;
    
    @Column(name = "publisher_place")
    private String publisherPlace;
    
    @Column(name = "rights")
    @Lob
    private String rights; // NIMAS usage rights
    
    @Column(name = "conversion_properties")
    @Convert(converter = ConversionPropertiesConverter.class)
    private ConversionProperties conversionProperties;
    
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PackageStatus status;
    
    @OneToMany(mappedBy = "nimasPackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<NimasFile> files = new ArrayList<>();
    
    @OneToMany(mappedBy = "nimasPackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ConversionJob> conversionJobs = new ArrayList<>();
    
    // Constructors
    public NimasPackage() {}
    
    public NimasPackage(String packageId, String title) {
        this.packageId = packageId;
        this.title = title;
        this.uploadedAt = LocalDateTime.now();
        this.status = PackageStatus.UPLOADED;
        this.conversionProperties = new ConversionProperties(); // Initialize with default properties
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPackageId() { return packageId; }
    public void setPackageId(String packageId) { this.packageId = packageId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getAudienceLevel() { return audienceLevel; }
    public void setAudienceLevel(String audienceLevel) { this.audienceLevel = audienceLevel; }
    
    public String getSourceEdition() { return sourceEdition; }
    public void setSourceEdition(String sourceEdition) { this.sourceEdition = sourceEdition; }
    
    public String getSourceDate() { return sourceDate; }
    public void setSourceDate(String sourceDate) { this.sourceDate = sourceDate; }
    
    public String getCopyrightDate() { return copyrightDate; }
    public void setCopyrightDate(String copyrightDate) { this.copyrightDate = copyrightDate; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getPublisherPlace() { return publisherPlace; }
    public void setPublisherPlace(String publisherPlace) { this.publisherPlace = publisherPlace; }
    
    public String getRights() { return rights; }
    public void setRights(String rights) { this.rights = rights; }
    
    public ConversionProperties getConversionProperties() { return conversionProperties; }
    public void setConversionProperties(ConversionProperties conversionProperties) { this.conversionProperties = conversionProperties; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    
    public PackageStatus getStatus() { return status; }
    public void setStatus(PackageStatus status) { this.status = status; }
    
    public List<NimasFile> getFiles() { return files; }
    public void setFiles(List<NimasFile> files) { this.files = files; }
    
    public List<ConversionJob> getConversionJobs() { return conversionJobs; }
    public void setConversionJobs(List<ConversionJob> conversionJobs) { this.conversionJobs = conversionJobs; }
    
    // Helper methods
    public void addFile(NimasFile file) {
        files.add(file);
        file.setNimasPackage(this);
    }
    
    public void addConversionJob(ConversionJob job) {
        conversionJobs.add(job);
        job.setNimasPackage(this);
    }
    
    public enum PackageStatus {
        UPLOADED,    // Package uploaded and extracted
        PROCESSING,  // Being processed for conversion
        READY,       // Ready for conversion
        ERROR        // Error during processing
    }
}
