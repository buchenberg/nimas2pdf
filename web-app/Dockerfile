# Multi-stage build for NIMAS2PDF Web Application
FROM maven:3.8.4-openjdk-17 AS backend-build

WORKDIR /app
COPY backend/pom.xml .
# Try to resolve dependencies with retry and better error handling
RUN mvn dependency:resolve -B || mvn clean compile -B

COPY backend/src ./src
RUN mvn clean package -DskipTests

# Frontend build stage
FROM node:18-alpine AS frontend-build

WORKDIR /app
COPY frontend/ ./
RUN npm ci
RUN npm run build

# Final runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Install necessary packages
RUN apt-get update && apt-get install -y \
    fontconfig \
    curl \
    postgresql-client \
    && rm -rf /var/lib/apt/lists/*

# Copy backend JAR
COPY --from=backend-build /app/target/*.jar app.jar

# Copy frontend build
COPY --from=frontend-build /app/build ./static

# Create necessary directories
RUN mkdir -p uploads outputs

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Copy wait-for script
COPY backend/wait-for-db.sh ./wait-for-db.sh
RUN chmod +x wait-for-db.sh

# Run the application with database wait
CMD ["./wait-for-db.sh", "postgres", "--", "java", "-jar", "app.jar"]
