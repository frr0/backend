# PowerShell script to start the Backend application
# This script will run the application with Maven Tomcat plugin

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Backend Application Startup" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Check if Maven is available
$mavenCheck = Get-Command mvn -ErrorAction SilentlyContinue
if ($mavenCheck) {
    $mavenCmd = "mvn"
    Write-Host "[INFO] Using system Maven" -ForegroundColor Green
} elseif (Test-Path "mvnw.cmd") {
    $mavenCmd = ".\mvnw.cmd"
    Write-Host "[INFO] Using Maven wrapper" -ForegroundColor Green
} else {
    Write-Host "ERROR: Neither Maven nor Maven wrapper found!" -ForegroundColor Red
    Write-Host "Please install Maven or make sure mvnw.cmd exists" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "[INFO] Starting application with Maven Tomcat plugin..." -ForegroundColor Green
Write-Host "[INFO] The application will be available at:" -ForegroundColor Green
Write-Host "       http://localhost:8080/backend/giocatori.html" -ForegroundColor Yellow
Write-Host ""
Write-Host "[INFO] Press Ctrl+C to stop the server" -ForegroundColor Green
Write-Host ""

# Run Maven Tomcat plugin
& $mavenCmd clean tomcat7:run

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: Failed to start the application" -ForegroundColor Red
    Write-Host ""
    Write-Host "Common issues:" -ForegroundColor Yellow
    Write-Host "  1. JAVA_HOME not set - Set it to your JDK installation folder" -ForegroundColor Yellow
    Write-Host "  2. Port 8080 already in use - Close other applications using this port" -ForegroundColor Yellow
    Write-Host "  3. Database connection issues - Make sure Oracle is running" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}
