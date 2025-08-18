# NIMAS2PDF

## What is it?

NIMAS2PDF is a Java swing based application that converts National Instructional Materials Access Standard files to Accessible PDF to be used by visually impaired primary students.

## Development status

This application has not been tested or actively developed for some time now. A stable version of this application is being successfully used by the Miami Accesible Media Project.

## Building the Application

NIMAS2PDF is a Java project built with Maven:

1. Make sure you have JDK 11 or later and Maven installed
2. From the `nimas2pdf` directory, run:

```sh
mvn clean package
```

This will:
- Compile the Java source files
- Run unit tests
- Package the application into an executable JAR file
- Include all dependencies

The built application will be available in the `target` directory as `nimas2pdf-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Running the Application

### Using Maven
```sh
mvn exec:java -Dexec.mainClass="org.eightfoldconsulting.nimas2pdf.FrameMain"
```

### Using the JAR directly
```sh
java -jar target/nimas2pdf-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Dependencies

The application uses several key libraries:
- Apache FOP for PDF generation
- Apache Batik for SVG support
- Apache XML Graphics Commons
- Apache Commons IO
- SLF4J with Logback for logging
- Xerces for XML parsing

All dependencies are managed through Maven in the `pom.xml` file.

## Development

The project uses:
- Java 11+
- Maven for dependency management and building
- Java Swing for the GUI
- SLF4J/Logback for logging
- JUnit for testing

Configuration files are stored in:
- `src/main/resources/conf/` - Application configuration including FOP config
- `src/main/resources/xml/` - XSLT stylesheets and DTDs for NIMAS processing

## Development Setup

1. Clone the repository:
```sh
git clone https://github.com/yourusername/nimas2pdf.git
cd nimas2pdf
```

2. Build the project:
```sh
mvn clean install
```

3. Open in your IDE (VS Code recommended)