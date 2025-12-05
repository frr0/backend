# Maven Dependency Reload Script
# This script will try to find Java and run Maven to download dependencies

Write-Host "Searching for Java installation..." -ForegroundColor Cyan

# Common Java installation paths
$javaPaths = @(
    "$env:ProgramFiles\Java\jdk-11*",
    "$env:ProgramFiles\Java\jdk-17*",
    "$env:ProgramFiles\Java\jdk-21*",
    "$env:ProgramFiles\Java\jdk11*",
    "$env:ProgramFiles\Java\jdk17*",
    "$env:ProgramFiles\Java\jdk1.11*",
    "${env:ProgramFiles(x86)}\Java\jdk-11*",
    "${env:ProgramFiles(x86)}\Java\jdk-17*",
    "C:\Program Files\Eclipse Adoptium\jdk-11*",
    "C:\Program Files\Eclipse Adoptium\jdk-17*"
)

$javaHome = $null
foreach ($path in $javaPaths) {
    $found = Get-Item $path -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($found) {
        $javaHome = $found.FullName
        Write-Host "Found Java at: $javaHome" -ForegroundColor Green
        break
    }
}

if (-not $javaHome) {
    Write-Host "ERROR: Java not found!" -ForegroundColor Red
    Write-Host "Please install Java 11 or later from:" -ForegroundColor Yellow
    Write-Host "  - https://adoptium.net/ (recommended)" -ForegroundColor Yellow
    Write-Host "  - https://www.oracle.com/java/technologies/downloads/" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Then run this script again." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Set JAVA_HOME for this session
$env:JAVA_HOME = $javaHome
Write-Host "Set JAVA_HOME=$env:JAVA_HOME" -ForegroundColor Green

# Try to run Maven wrapper
Write-Host ""
Write-Host "Running Maven to download dependencies..." -ForegroundColor Cyan
Write-Host "This may take a few minutes on first run..." -ForegroundColor Yellow
Write-Host ""

if (Test-Path ".\mvnw.cmd") {
    & .\mvnw.cmd clean compile -U
} elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
    mvn clean compile -U
} else {
    Write-Host "ERROR: Maven not found!" -ForegroundColor Red
    Write-Host "Maven wrapper (mvnw.cmd) is missing and Maven is not installed." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Done! Now try reloading your project in IntelliJ IDEA." -ForegroundColor Green
Write-Host "Right-click on pom.xml > Maven > Reload Project" -ForegroundColor Cyan
Write-Host ""
Read-Host "Press Enter to exit"

