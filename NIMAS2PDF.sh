#!/bin/bash

# Exit on error
set -e

# Check if Java is installed
if ! command -v java >/dev/null 2>&1; then
    echo "Error: Java is not installed or not in PATH"
    exit 1
fi

# Verify minimum Java version (using pattern matching instead of integer comparison)
JAVA_VERSION=$(java -version 2>&1 | grep -i version | cut -d'"' -f2)
if [[ "$JAVA_VERSION" == *"1.7"* ]] || [[ "$JAVA_VERSION" == *"1.6"* ]] || [[ "$JAVA_VERSION" == *"1.5"* ]]; then
    echo "Error: Java 8 or higher is required"
    exit 1
fi

# Get the directory where the script is located (handle symlinks)
SCRIPT_PATH=$(readlink -f "$0" 2>/dev/null || echo "$0")
SCRIPT_DIR=$(dirname "$SCRIPT_PATH")

# Check if required files exist
if [ ! -f "${SCRIPT_DIR}/NIMAS2PDF.jar" ]; then
    echo "Error: NIMAS2PDF.jar not found in ${SCRIPT_DIR}"
    exit 1
fi

if [ ! -d "${SCRIPT_DIR}/lib" ]; then
    echo "Error: lib directory not found in ${SCRIPT_DIR}"
    exit 1
fi

# Set up the classpath with all required libraries
CLASSPATH="${SCRIPT_DIR}/NIMAS2PDF.jar"
for jar in "${SCRIPT_DIR}"/lib/*.jar; do
    CLASSPATH="${CLASSPATH}:${jar}"
done

# Set memory options for Java
JAVA_OPTS="-Xms128m -Xmx512m"

# Add configuration directory to Java system properties
JAVA_OPTS="$JAVA_OPTS -Dapp.config.dir=${SCRIPT_DIR}/conf"

echo "Starting NIMAS2PDF from ${SCRIPT_DIR}..."
# Run the application
java $JAVA_OPTS -cp "$CLASSPATH" org.eightfoldconsulting.nimas2pdf.NIMAS2PDFApp