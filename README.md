# NIMAS2PDF Web Application

A web-based application for converting NIMAS (National Instructional Materials Accessibility Standard) packages to accessible PDF format. This application provides a complete workflow from package upload to PDF generation and download.

## 🚀 Features

- **NIMAS Package Upload**: Upload ZIP files containing NIMAS packages
- **PDF Conversion**: Convert NIMAS content to accessible PDF format
- **Job Management**: Monitor conversion progress and manage conversion jobs
- **Database Storage**: Persistent storage for packages, files, and conversion jobs
- **Modern UI**: React-based frontend with Bootstrap styling
- **RESTful API**: Spring Boot backend with comprehensive endpoints

## 🏗️ Architecture

The application consists of three main components:

- **Frontend**: React application with TypeScript
- **Backend**: Spring Boot Java application
- **Database**: PostgreSQL for persistent storage

## 📋 Prerequisites

- Docker and Docker Compose
- Java 17+ (for local development)
- Node.js 18+ (for local frontend development)
- Maven 3.8+ (for local backend development)

## 🐳 Quick Start with Docker Compose

The easiest way to run the application is using Docker Compose, which will set up all services automatically.

### 1. Clone the Repository

```bash
git clone <repository-url>
cd nimas2pdf
```

### 2. Start the Application

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f
```

### 3. Access the Application

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Database**: PostgreSQL on localhost:5432

### 4. Test with Sample Data

A sample NIMAS package is included for testing:
- **Sample Package**: `docs/exemplars/9781122334455NIMAS.zip`
- **Use this package** to test the complete workflow

### 5. Stop the Application

```bash
docker-compose down
```

## 🔧 Docker Compose Services

### Service Details

| Service | Port | Description | Status |
|---------|------|-------------|---------|
| `nimas2pdf-web` | 8080 | Spring Boot backend | 🟢 Running |
| `postgres` | 5432 | PostgreSQL database | 🟢 Running |

### Common Docker Compose Commands

```bash
# View running services
docker-compose ps

# View logs
docker-compose logs
docker-compose logs nimas2pdf-web
docker-compose logs postgres

# Restart services
docker-compose restart
docker-compose restart nimas2pdf-web

# Rebuild and start (after code changes)
docker-compose up --build -d

# Stop all services
docker-compose down

# Access database directly
docker-compose exec postgres psql -U nimas2pdf -d nimas2pdf
```

## 📖 Complete Workflow Guide

### Step 1: Upload a NIMAS Package

1. **Open the Application**: Navigate to http://localhost:3000
2. **Upload Tab**: The application opens on the Upload tab by default
3. **Select Package**: Click "Choose File" and select your NIMAS ZIP package
4. **Upload**: Click "Upload Package" to begin the upload process
5. **Success**: Upon successful upload, you'll be automatically redirected to the NIMAS Packages tab

**What Happens During Upload:**
- The ZIP file is extracted and validated
- OPF file is parsed for metadata
- All files are stored in the database
- Package status is set to "READY"

### Step 2: Start PDF Conversion

1. **Navigate to NIMAS Packages**: You'll be on this tab after upload
2. **View Package**: Your uploaded package will appear in the list
3. **Start Conversion**: Click "Start Conversion" on your package
4. **Monitor Progress**: The job will appear in the Conversion Jobs tab

**Conversion Process:**
- XML content is parsed and validated
- DTD references are resolved locally
- XSLT transformation converts XML to FO (Formatting Objects)
- FOP generates the final PDF
- Output is stored in the database

### Step 3: Download the Generated PDF

1. **Check Conversion Status**: Go to the Conversion Jobs tab
2. **Find Completed Job**: Look for jobs with "COMPLETED" status
3. **Download PDF**: Click the "Download" button on completed jobs
4. **File Saved**: The PDF will be downloaded to your computer

## 🔌 API Endpoints

### Package Management

```http
# Upload NIMAS Package
POST /api/packages/upload
Content-Type: multipart/form-data

# List All Packages
GET /api/packages

# Get Package Details
GET /api/packages/{id}

# Delete Package
DELETE /api/packages/{id}
```

### Conversion Jobs

```http
# Start Conversion
POST /api/conversion-jobs/start/{packageId}

# List All Jobs
GET /api/conversion-jobs

# Get Job Details
GET /api/conversion-jobs/{jobId}

# Download PDF
GET /api/conversion-jobs/{jobId}/download

# Cancel Job
POST /api/conversion-jobs/{jobId}/cancel

# Retry Failed Job
POST /api/conversion-jobs/{jobId}/retry
```

### Health Check

```http
# Application Health
GET /actuator/health
```

## 🛠️ Local Development

### Backend Development

```bash
cd backend

# Run with Maven
mvn spring-boot:run

# Run tests
mvn test

# Build JAR
mvn clean package
```

### Frontend Development

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start

# Run tests
npm test

# Build for production
npm run build
```

## 📁 Project Structure

```
nimas2pdf/
├── backend/                 # Spring Boot application
│   ├── src/main/java/      # Java source code
│   ├── src/main/resources/ # Configuration files
│   └── pom.xml            # Maven configuration
├── frontend/               # React application
│   ├── src/               # TypeScript source code
│   ├── public/            # Static assets
│   └── package.json       # Node.js dependencies
├── docker-compose.yml      # Docker services configuration
├── Dockerfile             # Multi-stage Docker build
├── uploads/               # Package upload directory
├── outputs/               # Generated PDF directory
├── docs/                  # Documentation and sample data
├── README.md              # This file
├── DATABASE.md            # Database documentation
└── TODO.md                # Development roadmap and improvements
```

## 🗄️ Database Schema

The application uses three main entities:

- **NimasPackage**: Stores package metadata and information
- **NimasFile**: Stores individual files within packages
- **ConversionJob**: Tracks conversion progress and results

For detailed database documentation, see [DATABASE.md](DATABASE.md).

## 🚀 Development Roadmap

We maintain a comprehensive development roadmap that outlines planned improvements, bug fixes, and new features. This roadmap is based on code reviews and user feedback.

### **Current Focus Areas**
- **Security & Stability**: Input validation, error handling, and security improvements
- **Performance & Scalability**: Service refactoring, caching, and async processing
- **Code Quality**: Testing coverage, documentation, and code standards
- **User Experience**: Frontend improvements and accessibility features

### **View the Roadmap**
For detailed information about planned improvements and development priorities, see [TODO.md](TODO.md).

**Key Priorities:**
1. **Phase 1**: Security vulnerabilities and stability fixes
2. **Phase 2**: Architecture improvements and performance optimization
3. **Phase 3**: Testing coverage and code quality tools
4. **Phase 4**: User experience enhancements and monitoring

## 🔍 Troubleshooting

### Common Issues

#### Frontend Can't Connect to Backend
- Ensure Docker Compose services are running: `docker-compose ps`
- Check backend health: `curl http://localhost:8080/actuator/health`
- Verify port mapping in docker-compose.yml

#### Upload Fails
- Check file size (max 100MB)
- Ensure ZIP contains valid NIMAS package structure
- Check backend logs: `docker-compose logs nimas2pdf-web`

#### Conversion Fails
- Verify DTD files are present in resources
- Check XML validity
- Review conversion job logs in the UI

#### Database Connection Issues
- Ensure PostgreSQL container is healthy: `docker-compose ps`
- Check database logs: `docker-compose logs postgres`
- Verify environment variables in docker-compose.yml

### Logs and Debugging

```bash
# View all logs
docker-compose logs

# Follow specific service logs
docker-compose logs -f nimas2pdf-web

# Check service status
docker-compose ps

# Restart problematic service
docker-compose restart nimas2pdf-web
```

## 🧪 Testing

### Backend Tests

```bash
cd backend
mvn test
```

### Frontend Tests

```bash
cd frontend
npm test
```

### End-to-End Testing

1. Start the application with Docker Compose
2. Upload a test NIMAS package (use the sample in `docs/exemplars/9781122334455NIMAS.zip`)
3. Start a conversion job
4. Download the generated PDF
5. Verify the PDF content and accessibility features

## 📝 Contributing

We welcome contributions! Before you start working on improvements, please review our development roadmap.

### **Getting Started**
1. **Review the Roadmap**: Check [TODO.md](TODO.md) for current priorities and planned improvements
2. **Fork the repository**
3. **Create a feature branch** for your changes
4. **Make your changes** following the established patterns
5. **Add tests** for new functionality
6. **Submit a pull request** with a clear description of your changes

### **Development Guidelines**
- **Security First**: Always consider security implications of your changes
- **Test Coverage**: Maintain or improve test coverage with your changes
- **Documentation**: Update relevant documentation when adding new features
- **Code Quality**: Follow existing code style and patterns

### **Current Priorities**
See [TODO.md](TODO.md) for the latest development priorities and areas that need attention.

## 📄 License

[Add your license information here]

## 🤝 Support

For issues and questions:
- Check the troubleshooting section above
- Review the logs for error details
- Open an issue in the repository

---

**NIMAS2PDF** - Making educational materials accessible through technology.
