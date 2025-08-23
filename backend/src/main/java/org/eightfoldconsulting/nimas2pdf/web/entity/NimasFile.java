package org.eightfoldconsulting.nimas2pdf.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity representing individual files within a NIMAS package.
 */
@Entity
@Table(name = "nimas_files")
public class NimasFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private NimasPackage nimasPackage;
    
    @Column(name = "file_path", nullable = false)
    private String filePath; // Relative path within the package
    
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "media_type", nullable = false)
    private String mediaType; // MIME type from OPF manifest
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "file_hash")
    private String fileHash; // SHA-256 hash for integrity checking
    
    @Column(name = "is_opf", nullable = false)
    private boolean isOpf; // Is this the OPF file?
    
    @Column(name = "is_dtbook", nullable = false)
    private boolean isDtbook; // Is this the main DTBook XML?
    
    @Column(name = "is_image", nullable = false)
    private boolean isImage; // Is this an image file?
    
    @Column(name = "is_pdf", nullable = false)
    private boolean isPdf; // Is this a PDF file?
    
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;
    
    @Column(name = "content", nullable = false)
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private byte[] content; // Binary file content
    
    // Constructors
    public NimasFile() {}
    
    public NimasFile(String filePath, String fileName, String mediaType, byte[] content) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.mediaType = mediaType;
        this.content = content;
        this.fileSize = (long) content.length;
        this.uploadedAt = LocalDateTime.now();
        
        // Determine file type based on media type and extension
        this.isOpf = mediaType.equals("application/oebps-package+xml") || fileName.endsWith(".opf");
        this.isDtbook = mediaType.equals("application/x-dtbook+xml") || fileName.endsWith(".xml");
        this.isImage = mediaType.startsWith("image/");
        this.isPdf = mediaType.equals("application/pdf") || fileName.endsWith(".pdf");
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public NimasPackage getNimasPackage() { return nimasPackage; }
    public void setNimasPackage(NimasPackage nimasPackage) { this.nimasPackage = nimasPackage; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }
    
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    
    public String getFileHash() { return fileHash; }
    public void setFileHash(String fileHash) { this.fileHash = fileHash; }
    
    public boolean isOpf() { return isOpf; }
    public void setOpf(boolean opf) { isOpf = opf; }
    
    public boolean isDtbook() { return isDtbook; }
    public void setDtbook(boolean dtbook) { isDtbook = dtbook; }
    
    public boolean isImage() { return isImage; }
    public void setImage(boolean image) { isImage = image; }
    
    public boolean isPdf() { return isPdf; }
    public void setPdf(boolean pdf) { isPdf = pdf; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    
    public byte[] getContent() { return content; }
    public void setContent(byte[] content) { 
        this.content = content; 
        this.fileSize = content != null ? (long) content.length : 0L;
    }
    
    // Helper methods
    public String getFullPath() {
        return filePath + "/" + fileName;
    }
    
    public boolean isTextFile() {
        return isOpf || isDtbook || mediaType.startsWith("text/");
    }
    
    public String getTextContent() {
        if (isTextFile() && content != null) {
            return new String(content);
        }
        return null;
    }
}
