@echo off
REM Quick Start Script for Backend Application

echo ====================================
echo Backend Application Quick Start
echo ====================================
echo.

REM Check if Maven is available
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven from https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo [1/3] Cleaning and building the application...
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo [2/3] Build successful! WAR file created in target\Backend-1.0-SNAPSHOT.war
echo.
echo [3/3] Next steps:
echo.
echo OPTION A - Deploy to Tomcat manually:
echo   1. Copy target\Backend-1.0-SNAPSHOT.war to Tomcat's webapps folder
echo   2. Rename it to backend.war
echo   3. Start Tomcat
echo   4. Access: http://localhost:8080/backend/giocatori.html
echo.
echo OPTION B - Use IntelliJ IDEA:
echo   1. Configure Tomcat in Run -^> Edit Configurations
echo   2. Set application context to /backend
echo   3. Run the configuration
echo.
echo OPTION C - Use Maven Tomcat plugin (if configured):
echo   Run: mvn tomcat7:run
echo   Access: http://localhost:8080/backend/giocatori.html
echo.
echo See DEPLOYMENT_INSTRUCTIONS.md for detailed instructions
echo.
pause

