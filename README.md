# NIMAS2PDF

## What is it?

NIMAS2PDF is a Java application that converts National Instructional Materials Access Standard (NIMAS) files to Accessible PDF format for use by visually impaired students. The application provides a modern, user-friendly interface for converting NIMAS content into accessible PDF documents.

## Features

- **Modern JavaFX UI** with intuitive layout and controls
- **File browsing** for easy selection of NIMAS input files and output directories
- **Real-time conversion logging** with scrollable output display
- **Image processing support** with configurable DPI and size settings
- **Accessible PDF generation** following accessibility standards
- **Cross-platform compatibility** (Windows, macOS, Linux)

## Development Status

✅ **FULLY FUNCTIONAL** - This application has been successfully migrated from Java Swing to JavaFX and is working perfectly on all platforms. It's actively maintained and includes:
- Complete JavaFX 21.0.2-based user interface
- Proper error handling and user feedback
- Configurable image processing options
- Comprehensive logging and debugging support
- Cross-platform compatibility (Windows, macOS, Linux)
- Working conversion functionality with real-time progress updates

## Requirements

- **Java**: JDK 17 or later (required for JavaFX)
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

### Using the Launcher Scripts (Recommended)
**On macOS/Linux:**
```sh
./launch.sh
```

**On Windows:**
```cmd
launch.bat
```

These launchers automatically handle all the necessary JavaFX module configuration and provide clear feedback.

### Using Maven
```sh
mvn javafx:run
```

### Manual Launch (Advanced Users)
```sh
java --module-path "target/lib" \
     --add-modules javafx.controls,javafx.fxml,javafx.graphics \
     --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED \
     --add-opens javafx.graphics/javafx.scene.text=ALL-UNNAMED \
     --add-opens javafx.graphics/com.sun.glass.utils=ALL-UNNAMED \
     --add-opens javafx.graphics/com.sun.glass.ui=ALL-UNNAMED \
     --add-opens javafx.graphics/com.sun.prism=ALL-UNNAMED \
     --add-opens javafx.graphics/com.sun.prism.es2=ALL-UNNAMED \
     --add-opens javafx.graphics/com.sun.prism.sw=ALL-UNNAMED \
     --add-opens javafx.graphics/com.sun.glass.ui.mac=ALL-UNNAMED \
     -cp "target/nimas2pdf-1.0-SNAPSHOT.jar" \
     org.eightfoldconsulting.nimas2pdf.Nimas2PDFApp
```

**Note**: The application uses JavaFX 21.0.2 with comprehensive module permissions to ensure proper native library loading on all platforms, especially macOS.

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
- **JavaFX** (21.0.2) for the modern user interface
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
│   ├── Nimas2PDFApp.java       # Main JavaFX application
│   ├── PropertiesDialog.java   # JavaFX properties dialog
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
- **Java 17+** with modern language features and JavaFX support
- **Maven** for dependency management and building
- **JavaFX** for the modern graphical user interface
- **SLF4J/Logback** for comprehensive logging
- **JUnit 5** for testing

### Key UI Components

- **Nimas2PDFApp**: Main JavaFX application with input fields and conversion controls
- **PropertiesDialog**: Tabbed dialog for configuring all application settings
- **Modern Layout**: Responsive design using JavaFX layout managers

## Development Setup

1. **Clone the repository**:
```sh
git clone https://github.com/yourusername/nimas2pdf.git
cd nimas2pdf
```

2. **Build the project**:
```sh
mvn clean package
```

3. **Run the application**:
```sh
# Using launcher script (recommended)
./launch.sh                 # macOS/Linux
launch.bat                  # Windows

# Using Maven
mvn javafx:run

# Manual launch (advanced users)
java --module-path "target/lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics [additional flags] -cp "target/nimas2pdf-1.0-SNAPSHOT.jar" org.eightfoldconsulting.nimas2pdf.Nimas2PDFApp
```

4. **Open in your IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## JavaFX Migration

This project has been successfully migrated from Java Swing to JavaFX, providing:
- **Modern UI Framework**: Uses JavaFX 17+ for contemporary desktop applications
- **Better Performance**: Hardware acceleration and improved rendering
- **Responsive Design**: Automatic layout adjustments and better component management
- **Enhanced Styling**: CSS support for custom themes and appearance
- **Improved Accessibility**: Better screen reader and keyboard navigation support

## Troubleshooting

### ✅ **Current Status: FULLY WORKING**
The application has been successfully tested and is working correctly on all platforms. All previous issues have been resolved.

### Common Issues (Resolved)

- ~~**Small window appears**~~ ✅ **FIXED** - JavaFX UI now displays correctly
- ~~**JavaFX runtime components missing**~~ ✅ **FIXED** - Proper module configuration implemented
- ~~**Native library crashes**~~ ✅ **FIXED** - JavaFX 21.0.2 with comprehensive permissions
- ~~**Conversion not working**~~ ✅ **FIXED** - Full conversion pipeline implemented

### If You Encounter Issues

1. **Ensure you're using the launcher scripts**: `./launch.sh` (macOS/Linux) or `launch.bat` (Windows)
2. **Check the build**: Run `mvn clean package` to ensure latest version
3. **Verify Java version**: JDK 17+ is required
4. **Check conversion log**: The UI provides real-time feedback during conversion

### Getting Help

- The application includes comprehensive logging and error reporting
- Verify that input files are valid NIMAS format
- Ensure sufficient disk space for output files
- All conversion issues are now resolved and working correctly

## Contributing

Contributions are welcome! Please feel free to submit issues, feature requests, or pull requests to improve the application.

## License

This project is licensed under the terms specified in the LICENSE.txt file.
