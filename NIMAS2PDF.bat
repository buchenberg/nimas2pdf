@echo off
setlocal EnableDelayedExpansion

REM Check if Java is installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Error: Java is not installed or not in PATH
    exit /b 1
)

REM Get Java version
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VER=%%g
)
set JAVA_VER=%JAVA_VER:"=%
for /f "delims=. tokens=1" %%v in ("%JAVA_VER%") do (
    if %%v LSS 8 (
        echo Error: Java 8 or higher is required
        exit /b 1
    )
)

REM Get the directory where the batch file is located
set SCRIPT_DIR=%~dp0

REM Check if required files exist
if not exist "%SCRIPT_DIR%NIMAS2PDF.jar" (
    echo Error: NIMAS2PDF.jar not found
    exit /b 1
)

if not exist "%SCRIPT_DIR%lib" (
    echo Error: lib directory not found
    exit /b 1
)

REM Set up the classpath with all required libraries
set CLASSPATH="%SCRIPT_DIR%NIMAS2PDF.jar"
for %%f in ("%SCRIPT_DIR%lib\*.jar") do (
    set CLASSPATH=!CLASSPATH!;"%%f"
)

REM Set memory options for Java
set JAVA_OPTS=-Xms128m -Xmx512m

REM Add configuration directory to Java system properties
set JAVA_OPTS=%JAVA_OPTS% -Dapp.config.dir="%SCRIPT_DIR%conf"

echo Starting NIMAS2PDF...
java %JAVA_OPTS% -cp %CLASSPATH% org.eightfoldconsulting.nimas2pdf.NIMAS2PDFApp