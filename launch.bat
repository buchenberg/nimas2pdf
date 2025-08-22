@echo off
echo NIMAS2PDF Launcher
echo ==================
echo.

REM Check if JAR exists
if not exist "target\nimas2pdf-1.0-SNAPSHOT.jar" (
    echo ❌ Error: JAR not found. Please build first:
    echo    mvn clean package
    pause
    exit /b 1
)

REM Check if lib directory exists
if not exist "target\lib" (
    echo ❌ Error: lib directory not found. Please build first:
    echo    mvn clean package
    pause
    exit /b 1
)

echo ✅ JAR and dependencies found
echo.

REM Try to launch with JavaFX modules (most reliable method)
echo 🚀 Launching NIMAS2PDF with JavaFX modules...
echo.

java --module-path "target\lib" ^
     --add-modules javafx.controls,javafx.fxml,javafx.graphics ^
     --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED ^
     --add-opens javafx.graphics/javafx.scene.text=ALL-UNNAMED ^
     --add-opens javafx.graphics/com.sun.glass.utils=ALL-UNNAMED ^
     --add-opens javafx.graphics/com.sun.glass.ui=ALL-UNNAMED ^
     --add-opens javafx.graphics/com.sun.prism=ALL-UNNAMED ^
     --add-opens javafx.graphics/com.sun.prism.es2=ALL-UNNAMED ^
     --add-opens javafx.graphics/com.sun.prism.sw=ALL-UNNAMED ^
     --add-opens javafx.graphics/com.sun.glass.ui.mac=ALL-UNNAMED ^
     -cp "target\nimas2pdf-1.0-SNAPSHOT.jar" ^
     org.eightfoldconsulting.nimas2pdf.Nimas2PDFApp

REM If we get here, the application has exited
echo.
echo Application has exited.
pause
