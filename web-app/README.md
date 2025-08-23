# NIMAS2PDF Web Application

## 🚀 **Overview**

This is the **web application version** of NIMAS2PDF, featuring a modern React frontend with Bootstrap UI and a Spring Boot REST API backend. It provides the same powerful NIMAS to PDF conversion capabilities as the desktop application, but accessible from any web browser.

**🆕 New in this version**: A comprehensive database layer for persistent storage of NIMAS packages, files, and conversion results, enabling enterprise-grade package management and workflow tracking.

## ✨ **Features**

- **🌐 Web-Based Interface**: Access from any device with a web browser
- **📁 Drag & Drop Upload**: Easy file upload with visual feedback
- **📦 NIMAS Package Support**: Upload and manage complete NIMAS ZIP packages
- **🗄️ Database Storage**: Persistent storage of packages, files, and conversion results
- **⚡ Real-Time Progress**: Monitor conversion progress in real-time
- **🔧 Configurable Properties**: Web-based settings management
- **📱 Responsive Design**: Works perfectly on desktop, tablet, and mobile
- **🔄 Batch Processing**: Convert multiple files efficiently
- **📊 Status Tracking**: Monitor all conversion jobs
- **💾 Download Results**: Easy access to converted PDFs
- **🔍 Package Management**: Search, filter, and organize NIMAS packages
- **✅ Validation**: Automatic validation of NIMAS package structure

## 🏗️ **Architecture**

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   React Frontend │    │  Spring Boot API │    │  NIMAS Engine   │
│   (Bootstrap)   │◄──►│   (REST/WebSocket)│◄──►│  (Existing)     │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

## 🛠️ **Technology Stack**

### **Backend**
- **Java 17** with **Spring Boot 3.2.0**
- **Spring Web** for REST endpoints
- **Spring WebSocket** for real-time updates
- **Spring Data JPA** for database operations
- **H2 Database** for development
- **PostgreSQL** for production
- **Apache Commons IO** for file operations
- **Note**: FOP, Batik, and other conversion dependencies will be added when integrating with existing NIMAS conversion logic

### **Frontend**
- **React 18** with **TypeScript**
- **Bootstrap 5** for responsive UI
- **React Bootstrap** for components
- **Axios** for HTTP requests
- **React Dropzone** for file uploads

## 🚀 **Quick Start**

### **Prerequisites**
- **Java 17+** (for backend)
- **Node.js 18+** (for frontend)
- **Maven 3.6+** (for backend build)
- **Docker & Docker Compose** (for containerized deployment)

### **1. Clone and Setup**
```bash
git clone <your-repo>
cd nimas2pdf/web-app
```

### **2. Backend Setup**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
Backend will be available at: `http://localhost:8080`

### **3. Frontend Setup**
```bash
cd frontend
npm install
npm start
```
Frontend will be available at: `http://localhost:3000`

### **4. Using Docker (Recommended)**
```bash
# Build and run with Docker Compose
docker-compose up --build

# Or build and run manually
docker build -t nimas2pdf-web .
docker run -p 8080:8080 nimas2pdf-web
```

## 📡 **API Endpoints**

### **Conversion API**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/convert` | Upload and convert NIMAS file |
| `GET` | `/api/status/{id}` | Get conversion status |
| `GET` | `/api/download/{id}` | Download converted PDF |
| `GET` | `/api/properties` | Get conversion properties |
| `POST` | `/api/properties` | Update conversion properties |

### **Package Management API**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/packages/upload` | Upload NIMAS ZIP package |
| `GET` | `/api/packages` | Get all packages |
| `GET` | `/api/packages/{id}` | Get package by ID |
| `GET` | `/api/packages/search` | Search packages by title |
| `GET` | `/api/packages/status/{status}` | Get packages by status |
| `GET` | `/api/packages/ready` | Get packages ready for conversion |
| `PUT` | `/api/packages/{id}/status` | Update package status |
| `DELETE` | `/api/packages/{id}` | Delete package |
| `GET` | `/api/packages/statistics` | Get package statistics |
| `GET` | `/api/packages/{id}/download` | Download package as ZIP |
| `GET` | `/api/packages/{id}/files` | Get package file content |
| `POST` | `/api/packages/{id}/validate` | Validate package structure |

## 🔧 **Configuration**

### **Database Connection**

#### **Development (H2 Database)**
- **URL**: `http://localhost:8081/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

#### **Production (PostgreSQL)**
- **Host**: `localhost`
- **Port**: `5432`
- **Database**: `nimas2pdf`
- **Username**: `nimas2pdf`
- **Password**: `password`

#### **Connection Examples**
```bash
# Using psql command line
psql -h localhost -p 5432 -U nimas2pdf -d nimas2pdf

# Using database client (pgAdmin, DBeaver, TablePlus)
# Use the same connection details as above
```

### **Backend Properties** (`application.properties`)
```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# File Upload
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

# File Storage
app.upload.dir=uploads
app.output.dir=outputs

# Conversion Settings
app.conversion.timeout=300000
app.conversion.max-concurrent=5
```

### **Frontend Configuration**
- **API Base URL**: Configured in `package.json` proxy
- **File Size Limits**: Handled by backend configuration
- **Supported Formats**: XML, ZIP files

## 📁 **Project Structure**

```
web-app/
├── backend/                          # Spring Boot Application
│   ├── src/main/java/
│   │   └── org/eightfoldconsulting/nimas2pdf/web/
│   │       ├── controller/           # REST Controllers
│   │       ├── service/              # Business Logic
│   │       ├── dto/                  # Data Transfer Objects
│   │       ├── entity/               # Database Entities
│   │       ├── repository/           # Data Access Layer
│   │       └── Nimas2PdfWebApplication.java
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── application-docker.properties
│   └── pom.xml
├── frontend/                         # React Application
│   ├── src/
│   │   ├── components/              # React Components
│   │   │   ├── FileUpload.tsx
│   │   │   ├── ConversionStatus.tsx
│   │   │   └── PropertiesPanel.tsx
│   │   ├── App.tsx                  # Main App Component
│   │   ├── index.tsx                # Entry Point
│   │   └── App.css                  # Styles
│   ├── public/
│   │   └── index.html
│   └── package.json
├── Dockerfile                        # Container Configuration
├── docker-compose.yml               # Docker Compose Setup
├── DATABASE.md                      # Database Documentation
└── README.md                        # This File
```

## 🔄 **Development Workflow**

### **Backend Development**
```bash
cd backend
mvn spring-boot:run
```
- Hot reload enabled
- Debug on port 5005
- Logs in console

### **Frontend Development**
```bash
cd frontend
npm start
```
- Hot reload enabled
- Development server on port 3000
- Proxy to backend on port 8080

### **Testing**
```bash
# Backend tests
cd backend
mvn test

# Frontend tests
cd frontend
npm test
```

## 🚀 **Deployment**

### **Production Build**
```bash
# Backend
cd backend
mvn clean package -Pprod

# Frontend
cd frontend
npm run build
```

### **Docker Deployment**
```bash
# Build image
docker build -t nimas2pdf-web .

# Run container
docker run -d -p 8080:8080 \
  -v /path/to/uploads:/app/uploads \
  -v /path/to/outputs:/app/outputs \
  nimas2pdf-web
```

### **Environment Variables**
```bash
SPRING_PROFILES_ACTIVE=prod
APP_UPLOAD_DIR=/app/uploads
APP_OUTPUT_DIR=/app/outputs
APP_CONVERSION_TIMEOUT=300000
APP_CONVERSION_MAX_CONCURRENT=5
```

## 🔍 **Monitoring & Health**

- **Health Check**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Logs**: Console and file-based logging
- **File Cleanup**: Automatic cleanup of old files

## 🐛 **Troubleshooting**

### **Common Issues**

1. **Port Conflicts**
   ```bash
   # Check what's using port 8080
   lsof -i :8080
   # Kill process or change port in application.properties
   ```

2. **File Permissions**
   ```bash
   # Ensure upload/output directories are writable
   chmod 755 uploads outputs
   ```

3. **Memory Issues**
   ```bash
   # Increase JVM heap size
   java -Xmx2g -jar app.jar
   ```

4. **CORS Issues**
   - Check `app.cors.allowed-origins` in properties
   - Ensure frontend URL is included

### **Logs**
```bash
# Backend logs
tail -f backend/logs/application.log

# Docker logs
docker logs nimas2pdf-web
```

### **Database Issues**

#### **PostgreSQL Connection Problems**
```bash
# Check if PostgreSQL container is running
docker-compose ps postgres

# Check PostgreSQL logs
docker-compose logs postgres

# Test database connection
docker exec -it web-app-postgres-1 psql -U nimas2pdf -d nimas2pdf
```

#### **Docker Networking Issues**
```bash
# Restart containers
docker-compose down
docker-compose up -d

# Rebuild containers
docker-compose down
docker-compose build --no-cache
docker-compose up -d

# Check container networking
docker network ls
docker network inspect web-app_default
```

#### **Database Schema Issues**
```bash
# Reset database (WARNING: This will delete all data)
docker-compose down -v
docker-compose up -d

# Check database tables
docker exec -it web-app-postgres-1 psql -U nimas2pdf -d nimas2pdf -c "\dt"
```

#### **Common Error Messages**

**"PostgreSQL is unavailable - sleeping"**
- Container networking issue
- PostgreSQL not ready yet
- Solution: Wait for health check or restart containers

**"could not translate host name 'postgres' to address"**
- DNS resolution issue in Docker network
- Solution: Ensure containers are on same network

**"connection refused"**
- PostgreSQL not running or wrong port
- Solution: Check container status and port mapping

## 🤝 **Contributing**

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 **License**

Same as the main NIMAS2PDF project.

## 🆘 **Support**

- **Issues**: GitHub Issues
- **Documentation**: This README
- **Community**: Project discussions

---

**🎉 Welcome to NIMAS2PDF Web Application!** 

This modern web interface brings the power of NIMAS conversion to the cloud, making it accessible from anywhere while maintaining all the robust features of the desktop version.
