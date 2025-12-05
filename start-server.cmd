@echo off
REM Simple batch script to start the server

echo =====================================
echo Backend Application Startup
echo =====================================
echo.

REM Check if Maven is available
where mvn >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    set MAVEN_CMD=mvn
    echo [INFO] Using system Maven
) else (
    REM Try Maven wrapper
    if exist "mvnw.cmd" (
        set MAVEN_CMD=mvnw.cmd
        echo [INFO] Using Maven wrapper
    ) else (
        echo ERROR: Neither Maven nor Maven wrapper found!
        echo Please install Maven or make sure mvnw.cmd exists
        pause
        exit /b 1
    )
)

echo.
echo Starting application with Maven Tomcat plugin...
echo The application will be available at:
echo     http://localhost:8080/backend/giocatori.html
echo.
echo Press Ctrl+C to stop the server
echo.

%MAVEN_CMD% clean tomcat7:run

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Failed to start the application
    echo.
    echo Common issues:
    echo   1. JAVA_HOME not set - Set it to your JDK installation folder
    echo   2. Port 8080 already in use - Close other applications using this port
    echo   3. Database connection issues - Make sure Oracle is running
    echo.
    pause
    exit /b 1
)

