# Database Layer Documentation

## Overview

The NIMAS2PDF web application now includes a comprehensive database layer for storing NIMAS packages, files, and conversion jobs. This layer provides persistent storage and efficient querying capabilities for managing the entire conversion workflow.

## Database Schema

### Core Entities

#### 1. NimasPackage
Represents a NIMAS package (OPF file) with all its metadata.

**Key Fields:**
- `id`: Primary key
- `packageId`: NIMAS identifier (e.g., "9781122334455NIMAS")
- `title`: Book title
- `creator`: Author(s)
- `language`: Language code
- `publisher`: Publisher name
- `format`: NIMAS version
- `source`: ISBN or source identifier
- `subject`: Subject area
- `audienceLevel`: Target education level
- `status`: Package status (UPLOADED, PROCESSING, READY, ERROR)
- `uploadedAt`: Upload timestamp

**Relationships:**
- One-to-Many with `NimasFile`
- One-to-Many with `ConversionJob`

#### 2. NimasFile
Represents individual files within a NIMAS package.

**Key Fields:**
- `id`: Primary key
- `nimasPackage`: Reference to parent package
- `filePath`: Relative path within package
- `fileName`: File name
- `mediaType`: MIME type
- `fileSize`: File size in bytes
- `fileHash`: SHA-256 hash for integrity
- `content`: Binary file content (BLOB)
- `isOpf`: Is this the OPF file?
- `isDtbook`: Is this the main DTBook XML?
- `isImage`: Is this an image file?
- `isPdf`: Is this a PDF file?

#### 3. ConversionJob
Tracks PDF conversion jobs and their results.

**Key Fields:**
- `id`: Primary key
- `jobId`: External job identifier (UUID)
- `nimasPackage`: Reference to package being converted
- `status`: Job status (PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED)
- `progress`: Progress percentage (0-100)
- `message`: Current status message
- `startedAt`: Job start time
- `completedAt`: Job completion time
- `errorMessage`: Error details if failed
- `conversionSettings`: JSON string of conversion parameters
- `outputFilename`: Generated PDF filename
- `outputSize`: Generated PDF size
- `outputContent`: Generated PDF content (BLOB)

## Database Configuration

### Development (H2)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Production (PostgreSQL)
```properties
spring.datasource.url=jdbc:postgresql://postgres:5432/nimas2pdf
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=nimas2pdf
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

## API Endpoints

### Package Management

#### Upload NIMAS Package
```http
POST /api/packages/upload
Content-Type: multipart/form-data

file: [ZIP file containing NIMAS package]
```

**Response:**
```json
{
  "id": 1,
  "packageId": "9781122334455NIMAS",
  "title": "The Great Depression",
  "creator": "John Smith, Jane Doe",
  "language": "en",
  "publisher": "CAST, Inc.",
  "format": "NIMAS 1.1",
  "source": "9781122334455",
  "subject": "Social Studies",
  "status": "READY",
  "uploadedAt": "2024-01-15T10:30:00"
}
```

#### Get All Packages
```http
GET /api/packages
```

#### Get Package by ID
```http
GET /api/packages/{id}
```

#### Search Packages
```http
GET /api/packages/search?title=depression
```

#### Get Packages by Status
```http
GET /api/packages/status/READY
```

#### Get Packages Ready for Conversion
```http
GET /api/packages/ready
```

#### Update Package Status
```http
PUT /api/packages/{id}/status?status=PROCESSING
```

#### Delete Package
```http
DELETE /api/packages/{id}
```

#### Get Package Statistics
```http
GET /api/packages/statistics
```

**Response:**
```json
{
  "totalPackages": 25,
  "uploadedPackages": 5,
  "readyPackages": 15,
  "processingPackages": 3,
  "errorPackages": 2
}
```

#### Download Package as ZIP
```http
GET /api/packages/{id}/download
```

#### Get Package File Content
```http
GET /api/packages/{id}/files?filePath=images/U01C01&fileName=p002-001.jpg
```

#### Validate Package Structure
```http
POST /api/packages/{id}/validate
```

**Response:**
```json
{
  "valid": true,
  "errors": [],
  "warnings": ["No CSS files found"]
}
```

## NIMAS Package Structure

### Expected ZIP Contents
```
package.zip
├── package.opf                    # OPF manifest file
├── content.xml                    # Main DTBook XML
├── images/
│   ├── cover.jpg
│   ├── page1.jpg
│   └── page2.jpg
└── styles/
    └── styles.css
```

### OPF File Requirements
- Must contain `dc:Title` element
- Must contain `dc:Identifier` with scheme="NIMAS"
- Must contain `dc:Format` indicating NIMAS version
- Must contain `dc:Language` element
- Should contain `dc:Creator` element
- Should contain `dc:Subject` element

### Validation Rules
1. **Required Files:**
   - OPF file (`.opf` extension)
   - DTBook XML file (`.xml` extension)

2. **Required Metadata:**
   - Package ID (NIMAS identifier)
   - Title
   - Format (NIMAS version)

3. **File Type Detection:**
   - OPF files: `application/oebps-package+xml`
   - DTBook XML: `application/x-dtbook+xml`
   - Images: `image/jpeg`, `image/png`, `image/gif`
   - PDFs: `application/pdf`

## Database Operations

### Package Upload Process
1. **ZIP Extraction:** Extract all files from uploaded ZIP
2. **File Analysis:** Determine file types and media types
3. **OPF Parsing:** Parse OPF file to extract metadata
4. **Validation:** Validate package structure and metadata
5. **Storage:** Store package, files, and metadata in database
6. **Status Update:** Mark package as READY for conversion

### File Storage Strategy
- **Binary Content:** Stored as BLOB in database
- **File Hashing:** SHA-256 hash for integrity verification
- **Lazy Loading:** Large content loaded only when needed
- **Compression:** Consider implementing compression for large files

### Conversion Job Management
1. **Job Creation:** Create conversion job for READY package
2. **Status Tracking:** Monitor progress and update status
3. **Result Storage:** Store generated PDF in database
4. **Cleanup:** Option to remove source files after conversion

## Performance Considerations

### Database Indexing
- `packageId` (unique)
- `status` (for filtering)
- `uploadedAt` (for date-based queries)
- `filePath` + `fileName` (for file lookups)

### Query Optimization
- Use lazy loading for large BLOB content
- Implement pagination for large result sets
- Use appropriate fetch strategies for relationships

### Storage Management
- Implement file size limits
- Consider external file storage for very large files
- Implement cleanup policies for old packages

## Security Considerations

### File Upload Validation
- Validate file types and extensions
- Check file size limits
- Scan for malicious content
- Implement virus scanning

### Access Control
- Implement user authentication
- Role-based access control
- Package ownership and sharing

### Data Privacy
- Encrypt sensitive metadata
- Implement audit logging
- Data retention policies

## Monitoring and Maintenance

### Health Checks
- Database connectivity
- File storage availability
- Conversion service status

### Metrics
- Package upload rates
- Conversion success rates
- Storage usage
- Response times

### Backup and Recovery
- Regular database backups
- File content backup strategies
- Disaster recovery procedures

## Future Enhancements

### Planned Features
- **Versioning:** Track package versions and updates
- **Collaboration:** Multi-user package management
- **Workflows:** Advanced conversion workflows
- **Analytics:** Usage analytics and reporting
- **Integration:** External system integrations

### Scalability Improvements
- **Sharding:** Database sharding for large deployments
- **Caching:** Redis caching for frequently accessed data
- **CDN:** Content delivery network for file downloads
- **Microservices:** Break down into smaller services

## Troubleshooting

### Common Issues
1. **Large File Uploads:** Increase `spring.servlet.multipart.max-file-size`
2. **Database Connection:** Check connection pool settings
3. **Memory Issues:** Monitor JVM heap size and garbage collection
4. **File Corruption:** Verify file hashes and implement retry logic

### Debug Tools
- H2 Console (development): `/h2-console`
- Database logs: Enable SQL logging
- Application logs: Configure appropriate log levels
- Health endpoints: `/actuator/health`

## Migration Guide

### From File-Based Storage
1. **Data Migration:** Script to migrate existing files
2. **Schema Updates:** Database schema changes
3. **API Updates:** Update client applications
4. **Testing:** Comprehensive testing of new functionality

### Version Compatibility
- Maintain backward compatibility where possible
- Provide migration scripts for data updates
- Document breaking changes clearly
