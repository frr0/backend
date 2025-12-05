@echo off
REM Quick Start Script - Sets JAVA_HOME and starts server

echo =====================================
echo Backend Application - QUICK START
echo =====================================
echo.

REM Set JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk-25.0.1
echo JAVA_HOME set to: %JAVA_HOME%
echo.

echo Starting server...
echo.
echo Your webapp will be available at:
echo http://localhost:8080/backend/giocatori.html
echo.
echo Wait for "Running war on http://localhost:8080/backend" message
echo Press Ctrl+C to stop the server
echo.

mvnw.cmd tomcat7:run

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Failed to start the server
    echo.
    echo If you see "port already in use", the server might already be running!
    echo Try opening: http://localhost:8080/backend/giocatori.html
    echo.
    pause
    exit /b 1
)

