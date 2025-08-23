package org.eightfoldconsulting.nimas2pdf.web.service;

import org.eightfoldconsulting.nimas2pdf.web.entity.NimasFile;
import org.eightfoldconsulting.nimas2pdf.web.entity.NimasPackage;
import org.eightfoldconsulting.nimas2pdf.web.repository.NimasFileRepository;
import org.eightfoldconsulting.nimas2pdf.web.repository.NimasPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Implementation of NimasPackageService.
 */
@Service
@Transactional
public class NimasPackageServiceImpl implements NimasPackageService {
    
    @Autowired
    private NimasPackageRepository packageRepository;
    
    @Autowired
    private NimasFileRepository fileRepository;
    
    @Override
    public NimasPackage uploadPackage(MultipartFile zipFile) throws Exception {
        // Create package entity
        NimasPackage nimasPackage = new NimasPackage();
        nimasPackage.setStatus(NimasPackage.PackageStatus.UPLOADED);
        nimasPackage.setUploadedAt(java.time.LocalDateTime.now());
        
        // Save package first to get ID
        nimasPackage = packageRepository.save(nimasPackage);
        
        try {
            // Extract ZIP contents
            List<NimasFile> extractedFiles = extractZipContents(zipFile.getInputStream(), nimasPackage);
            
            // Find and parse OPF file
            Optional<NimasFile> opfFile = extractedFiles.stream()
                .filter(NimasFile::isOpf)
                .findFirst();
            
            if (opfFile.isPresent()) {
                // Extract metadata from OPF
                extractPackageMetadataFromOpf(opfFile.get(), nimasPackage);
                
                // Validate package structure
                PackageValidationResult validation = validatePackage(nimasPackage);
                if (!validation.isValid()) {
                    nimasPackage.setStatus(NimasPackage.PackageStatus.ERROR);
                    packageRepository.save(nimasPackage);
                    throw new RuntimeException("Invalid NIMAS package: " + String.join(", ", validation.getErrors()));
                }
                
                // Update package status
                nimasPackage.setStatus(NimasPackage.PackageStatus.READY);
                packageRepository.save(nimasPackage);
                
                return nimasPackage;
            } else {
                throw new RuntimeException("No OPF file found in NIMAS package");
            }
            
        } catch (Exception e) {
            nimasPackage.setStatus(NimasPackage.PackageStatus.ERROR);
            packageRepository.save(nimasPackage);
            throw e;
        }
    }
    
    @Override
    public Optional<NimasPackage> getPackageById(Long id) {
        return packageRepository.findById(id);
    }
    
    @Override
    public Optional<NimasPackage> getPackageByNimasId(String nimasId) {
        return packageRepository.findByPackageId(nimasId);
    }
    
    @Override
    public List<NimasPackage> getAllPackages() {
        return packageRepository.findAll();
    }
    
    @Override
    public List<NimasPackage> searchPackagesByTitle(String title) {
        return packageRepository.findByTitleContainingIgnoreCase(title);
    }
    
    @Override
    public List<NimasPackage> getPackagesByStatus(NimasPackage.PackageStatus status) {
        return packageRepository.findByStatus(status);
    }
    
    @Override
    public List<NimasPackage> getPackagesReadyForConversion() {
        return packageRepository.findPackagesReadyForConversion();
    }
    
    @Override
    public NimasPackage updatePackageStatus(Long packageId, NimasPackage.PackageStatus status) {
        Optional<NimasPackage> packageOpt = packageRepository.findById(packageId);
        if (packageOpt.isPresent()) {
            NimasPackage nimasPackage = packageOpt.get();
            nimasPackage.setStatus(status);
            return packageRepository.save(nimasPackage);
        }
        throw new RuntimeException("Package not found: " + packageId);
    }
    
    @Override
    public void deletePackage(Long packageId) {
        packageRepository.deleteById(packageId);
    }
    
    @Override
    public PackageStatistics getPackageStatistics() {
        long total = packageRepository.count();
        long uploaded = packageRepository.countByStatus(NimasPackage.PackageStatus.UPLOADED);
        long ready = packageRepository.countByStatus(NimasPackage.PackageStatus.READY);
        long processing = packageRepository.countByStatus(NimasPackage.PackageStatus.PROCESSING);
        long error = packageRepository.countByStatus(NimasPackage.PackageStatus.ERROR);
        
        return new PackageStatistics(total, uploaded, ready, processing, error);
    }
    
    @Override
    public PackageValidationResult validatePackage(NimasPackage nimasPackage) {
        PackageValidationResult result = new PackageValidationResult();
        
        // Check if package has OPF file
        List<NimasFile> opfFiles = fileRepository.findByNimasPackageIdAndIsOpfTrue(nimasPackage.getId());
        if (opfFiles.isEmpty()) {
            result.addError("No OPF file found");
        }
        
        // Check if package has DTBook XML
        Optional<NimasFile> dtbookFile = fileRepository.findByNimasPackageIdAndIsDtbookTrue(nimasPackage.getId());
        if (dtbookFile.isEmpty()) {
            result.addError("No DTBook XML file found");
        }
        
        // Check if package has required metadata
        if (nimasPackage.getPackageId() == null || nimasPackage.getPackageId().trim().isEmpty()) {
            result.addError("Package ID is required");
        }
        
        if (nimasPackage.getTitle() == null || nimasPackage.getTitle().trim().isEmpty()) {
            result.addError("Title is required");
        }
        
        return result;
    }
    
    @Override
    public void extractPackageMetadata(NimasPackage nimasPackage) {
        // This method is called during upload processing
        // Implementation is in extractPackageMetadataFromOpf
    }
    
    @Override
    public Resource downloadPackageAsZip(Long packageId) throws Exception {
        Optional<NimasPackage> packageOpt = packageRepository.findById(packageId);
        if (packageOpt.isEmpty()) {
            throw new RuntimeException("Package not found: " + packageId);
        }
        
        NimasPackage nimasPackage = packageOpt.get();
        List<NimasFile> files = fileRepository.findByNimasPackageId(packageId);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(baos);
        
        for (NimasFile file : files) {
            ZipEntry entry = new ZipEntry(file.getFilePath() + "/" + file.getFileName());
            zos.putNextEntry(entry);
            zos.write(file.getContent());
            zos.closeEntry();
        }
        
        zos.close();
        return new ByteArrayResource(baos.toByteArray());
    }
    
    @Override
    public Resource getPackageFileContent(Long packageId, String filePath, String fileName) throws Exception {
        Optional<NimasFile> fileOpt = fileRepository.findByNimasPackageIdAndFilePathAndFileName(packageId, filePath, fileName);
        if (fileOpt.isPresent()) {
            NimasFile file = fileOpt.get();
            return new ByteArrayResource(file.getContent());
        }
        throw new RuntimeException("File not found: " + filePath + "/" + fileName);
    }
    
    /**
     * Extract contents from ZIP file and create NimasFile entities.
     */
    private List<NimasFile> extractZipContents(java.io.InputStream inputStream, NimasPackage nimasPackage) throws IOException {
        List<NimasFile> files = new ArrayList<>();
        
        try (ZipInputStream zis = new ZipInputStream(inputStream)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    // Read file content
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        baos.write(buffer, 0, len);
                    }
                    
                    byte[] content = baos.toByteArray();
                    
                    // Determine media type
                    String mediaType = determineMediaType(entry.getName());
                    
                    // Create NimasFile entity
                    NimasFile nimasFile = new NimasFile(
                        getFilePath(entry.getName()),
                        getFileName(entry.getName()),
                        mediaType,
                        content
                    );
                    
                    // Calculate file hash
                    nimasFile.setFileHash(calculateFileHash(content));
                    
                    // Associate with package
                    nimasPackage.addFile(nimasFile);
                    
                    // Save file
                    fileRepository.save(nimasFile);
                    files.add(nimasFile);
                }
                zis.closeEntry();
            }
        }
        
        return files;
    }
    
    /**
     * Extract metadata from OPF file and update package.
     */
    private void extractPackageMetadataFromOpf(NimasFile opfFile, NimasPackage nimasPackage) throws Exception {
        String opfContent = new String(opfFile.getContent(), StandardCharsets.UTF_8);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(opfFile.getContent()));
        
        // Extract basic metadata
        NodeList titleNodes = document.getElementsByTagName("dc:Title");
        if (titleNodes.getLength() > 0) {
            nimasPackage.setTitle(titleNodes.item(0).getTextContent());
        }
        
        NodeList creatorNodes = document.getElementsByTagName("dc:Creator");
        if (creatorNodes.getLength() > 0) {
            nimasPackage.setCreator(creatorNodes.item(0).getTextContent());
        }
        
        NodeList languageNodes = document.getElementsByTagName("dc:Language");
        if (languageNodes.getLength() > 0) {
            nimasPackage.setLanguage(languageNodes.item(0).getTextContent());
        }
        
        NodeList publisherNodes = document.getElementsByTagName("dc:Publisher");
        if (publisherNodes.getLength() > 0) {
            nimasPackage.setPublisher(publisherNodes.item(0).getTextContent());
        }
        
        NodeList formatNodes = document.getElementsByTagName("dc:Format");
        if (formatNodes.getLength() > 0) {
            nimasPackage.setFormat(formatNodes.item(0).getTextContent());
        }
        
        NodeList sourceNodes = document.getElementsByTagName("dc:Source");
        if (sourceNodes.getLength() > 0) {
            nimasPackage.setSource(sourceNodes.item(0).getTextContent());
        }
        
        NodeList subjectNodes = document.getElementsByTagName("dc:Subject");
        if (subjectNodes.getLength() > 0) {
            nimasPackage.setSubject(subjectNodes.item(0).getTextContent());
        }
        
        NodeList rightsNodes = document.getElementsByTagName("dc:Rights");
        if (rightsNodes.getLength() > 0) {
            nimasPackage.setRights(rightsNodes.item(0).getTextContent());
        }
        
        // Extract extended metadata
        NodeList metaNodes = document.getElementsByTagName("meta");
        for (int i = 0; i < metaNodes.getLength(); i++) {
            Element metaElement = (Element) metaNodes.item(i);
            String name = metaElement.getAttribute("name");
            String content = metaElement.getAttribute("content");
            
            switch (name) {
                case "nimas-SourceEdition":
                    nimasPackage.setSourceEdition(content);
                    break;
                case "nimas-SourceDate":
                    nimasPackage.setSourceDate(content);
                    break;
                case "DCTERMS.audience.educationLevel":
                    nimasPackage.setAudienceLevel(content);
                    break;
                case "DCTERMS.date.dateCopyrighted":
                    nimasPackage.setCopyrightDate(content);
                    break;
                case "DCTERMS.description.note":
                    if (nimasPackage.getDescription() == null) {
                        nimasPackage.setDescription(content);
                    } else {
                        nimasPackage.setDescription(nimasPackage.getDescription() + "; " + content);
                    }
                    break;
                case "DCTERMS.description.version":
                    nimasPackage.setVersion(content);
                    break;
                case "DCTERMS.publisher.place":
                    nimasPackage.setPublisherPlace(content);
                    break;
            }
        }
        
        // Extract package ID from identifier
        NodeList identifierNodes = document.getElementsByTagName("dc:Identifier");
        for (int i = 0; i < identifierNodes.getLength(); i++) {
            Element idElement = (Element) identifierNodes.item(i);
            String scheme = idElement.getAttribute("scheme");
            if ("NIMAS".equals(scheme)) {
                nimasPackage.setPackageId(idElement.getTextContent());
                break;
            }
        }
        
        // Save updated package
        packageRepository.save(nimasPackage);
    }
    
    /**
     * Determine media type based on file extension.
     */
    private String determineMediaType(String fileName) {
        String lowerFileName = fileName.toLowerCase();
        if (lowerFileName.endsWith(".opf")) {
            return "application/oebps-package+xml";
        } else if (lowerFileName.endsWith(".xml")) {
            return "application/x-dtbook+xml";
        } else if (lowerFileName.endsWith(".jpg") || lowerFileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerFileName.endsWith(".png")) {
            return "image/png";
        } else if (lowerFileName.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerFileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (lowerFileName.endsWith(".css")) {
            return "text/css";
        } else {
            return "application/octet-stream";
        }
    }
    
    /**
     * Extract file path from full path.
     */
    private String getFilePath(String fullPath) {
        int lastSlash = fullPath.lastIndexOf('/');
        return lastSlash > 0 ? fullPath.substring(0, lastSlash) : "";
    }
    
    /**
     * Extract file name from full path.
     */
    private String getFileName(String fullPath) {
        int lastSlash = fullPath.lastIndexOf('/');
        return lastSlash > 0 ? fullPath.substring(lastSlash + 1) : fullPath;
    }
    
    /**
     * Calculate SHA-256 hash of file content.
     */
    private String calculateFileHash(byte[] content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
