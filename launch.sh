#!/bin/bash

echo "NIMAS2PDF Launcher"
echo "=================="
echo ""

# Check if JAR exists
if [ ! -f "target/nimas2pdf-1.0-SNAPSHOT.jar" ]; then
    echo "❌ Error: JAR not found. Please build first:"
    echo "   mvn clean package"
    exit 1
fi

# Check if lib directory exists
if [ ! -d "target/lib" ]; then
    echo "❌ Error: lib directory not found. Please build first:"
    echo "   mvn clean package"
    exit 1
fi

echo "✅ JAR and dependencies found"
echo ""

# Try to launch with JavaFX modules (most reliable method)
echo "🚀 Launching NIMAS2PDF with JavaFX modules..."
echo ""

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

# If we get here, the application has exited
echo ""
echo "Application has exited."
