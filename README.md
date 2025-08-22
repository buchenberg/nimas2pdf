# NIMAS2PDF

## What is it?

NIMAS2PDF is a Java application that converts National Instructional Materials Access Standard (NIMAS) files to Accessible PDF format for use by visually impaired students. The application provides a modern, user-friendly interface for converting NIMAS content into accessible PDF documents.

## Features

- **Modern Java Swing UI** with intuitive layout and controls
- **File browsing** for easy selection of NIMAS input files and output directories
- **Real-time conversion logging** with scrollable output display
- **Image processing support** with configurable DPI and size settings
- **Accessible PDF generation** following accessibility standards
- **Cross-platform compatibility** (Windows, macOS, Linux)

## Development Status

This application has been recently updated with a modern UI and improved functionality. It's actively maintained and includes:
- Complete Swing-based user interface
- Proper error handling and user feedback
- Configurable image processing options
- Comprehensive logging and debugging support

## Requirements

- **Java**: JDK 11 or later
- **Maven**: 3.6+ for building
- **Memory**: Minimum 512MB RAM recommended
- **Disk Space**: At least 100MB free space

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

### Using the JAR directly (Recommended)
```sh
java -jar target/nimas2pdf-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Using Maven
```sh
mvn exec:java -Dexec.mainClass="org.eightfoldconsulting.nimas2pdf.FrameMain"
```

## Using the Application

1. **Launch the application** using one of the methods above
2. **Select NIMAS Input**: Click "Browse..." next to "NIMAS File/Path" to select your NIMAS file or directory
3. **Choose Output Directory**: Click "Browse..." next to "Output Directory" to select where to save the generated PDF
4. **Convert**: Click the "Convert" button to start the conversion process
5. **Monitor Progress**: Watch the conversion log for real-time updates and any error messages

### Advanced Options

- **Image Settings**: Access image processing options through the options menu
- **DPI Configuration**: Adjust image resolution for optimal quality
- **Size Limits**: Set maximum dimensions for processed images

## Dependencies

The application uses several key libraries:
- **Apache FOP** (2.8) for PDF generation
- **Apache Batik** (1.16) for SVG support and image processing
- **Apache Commons IO** (2.13.0) for file operations
- **SLF4J with Logback** for comprehensive logging
- **JEuclid** for MathML support
- **Apache Avalon Framework** for component management

All dependencies are managed through Maven in the `pom.xml` file.

## Project Structure

```
nimas2pdf/
├── src/main/java/org/eightfoldconsulting/nimas2pdf/
│   ├── FrameMain.java          # Main application window
│   ├── FrameOptions.java       # Options dialog
│   ├── FrameImageResize.java   # Image settings dialog
│   ├── XMLConverter.java       # Core conversion logic
│   ├── OPFReader.java          # NIMAS OPF file parser
│   └── config/                 # Configuration classes
├── src/main/resources/
│   ├── conf/                   # FOP configuration
│   ├── xml/                    # XSLT stylesheets and DTDs
│   └── application.properties  # Application settings
└── pom.xml                     # Maven configuration
```

## Development

The project uses:
- **Java 11+** with modern language features
- **Maven** for dependency management and building
- **Java Swing** for the graphical user interface
- **SLF4J/Logback** for comprehensive logging
- **JUnit 5** for testing

### Key UI Components

- **FrameMain**: Main application window with input fields and conversion controls
- **FrameOptions**: Dialog for configuring image processing options
- **FrameImageResize**: Advanced image settings configuration

## Development Setup

1. **Clone the repository**:
```sh
git clone https://github.com/yourusername/nimas2pdf.git
cd nimas2pdf
```

2. **Build the project**:
```sh
mvn clean install
```

3. **Run the application**:
```sh
java -jar target/nimas2pdf-1.0-SNAPSHOT-jar-with-dependencies.jar
```

4. **Open in your IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## Troubleshooting

### Common Issues

- **Small window appears**: Ensure you're using the latest build with the updated UI
- **Conversion fails**: Check the log output for specific error messages
- **Memory issues**: Increase JVM heap size with `-Xmx1g` flag

### Getting Help

- Check the conversion log for detailed error information
- Verify that input files are valid NIMAS format
- Ensure sufficient disk space for output files

## Contributing

Contributions are welcome! Please feel free to submit issues, feature requests, or pull requests to improve the application.

## License

This project is licensed under the terms specified in the LICENSE.txt file.
