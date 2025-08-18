# NIMAS2PDF

## What is it?

NIMAS2PDF is a Java swing based application that converts National Instructional Materials Access Standard files to Accessible PDF to be used by visually impaired primary students.

## Development status

This application has not been tested or actively developed for some time now. A stable version of this application is being successfully used by the Miami Accesible Media Project.

## Building the Application

NIMAS2PDF is a Java project built with Apache Ant. To build the application:

1. Make sure you have JDK and Apache Ant installed
2. From the `NIMAS2PDF` directory, run:

```sh
ant jar
```

This will:
- Compile the Java source files
- Package the application into a JAR file
- Copy required configuration files and resources
- Create startup scripts (NIMAS2PDF.bat for Windows and NIMAS2PDF.sh for Linux)

The built application will be available in the `dist` directory.

## Running the Application

### Windows
Double-click `NIMAS2PDF.bat` or run from command line:
```sh
NIMAS2PDF.bat
```

### Linux/Unix
Make the script executable and run:
```sh
chmod +x NIMAS2PDF.sh
./NIMAS2PDF.sh
```

## Dependencies

The application uses several key libraries:
- Apache FOP for PDF generation
- Apache Batik for SVG support
- Apache XML Graphics Commons
- Apache Commons IO and Logging
- Xerces for XML parsing

All required libraries are included in the `lib` directory.

## Development

The project uses:
- Java Swing for the GUI
- NetBeans project structure
- Apache Ant for building
- Java logging for diagnostic output

Configuration files are stored in:
- `conf/` - Application configuration including FOP config
- `xml/` - XSLT stylesheets and DTDs for NIMAS processing



