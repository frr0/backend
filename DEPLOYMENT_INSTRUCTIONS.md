# Deployment Instructions

## Problem
You're getting ERR_CONNECTION_REFUSED when trying to access `http://localhost/backend/giocatori.html`

## Root Cause
No web server is running on port 80 (or 8080) with your application deployed.

## Solution

### Option 1: Using IntelliJ IDEA with Tomcat (Recommended)

1. **Download Apache Tomcat**
   - Download Tomcat 10.x from https://tomcat.apache.org/download-10.cgi
   - Extract it to a folder (e.g., `C:\apache-tomcat-10.x.x`)

2. **Configure IntelliJ IDEA**
   - Go to `Run` → `Edit Configurations`
   - Click the `+` button → `Tomcat Server` → `Local`
   - Click `Configure...` next to Application Server
   - Point to your Tomcat installation folder
   - Click `OK`

3. **Configure Deployment**
   - In the Run Configuration window, go to the `Deployment` tab
   - Click the `+` button → `Artifact` → Select `Backend:war exploded`
   - Set **Application context** to `/backend` (This is important!)
   - Click `Apply` and `OK`

4. **Configure Server Port (if needed)**
   - In the `Server` tab, check the HTTP port
   - If you want to use port 80 (like your professor), change it to 80
   - Default is usually 8080, which means you'd access: `http://localhost:8080/backend/giocatori.html`
   - **Note:** Port 80 may require administrator privileges on Windows

5. **Run the Application**
   - Click the green Run button
   - Wait for Tomcat to start
   - Access: `http://localhost/backend/giocatori.html` (if port 80) or `http://localhost:8080/backend/giocatori.html` (if port 8080)

### Option 2: Manual Tomcat Deployment

1. **Build the WAR file**
   ```powershell
   mvn clean package
   ```
   This creates `Backend-1.0-SNAPSHOT.war` in the `target` folder

2. **Rename the WAR file**
   - Rename `Backend-1.0-SNAPSHOT.war` to `backend.war` (the filename becomes the context path)

3. **Deploy to Tomcat**
   - Copy `backend.war` to Tomcat's `webapps` folder
   - Start Tomcat (run `bin/startup.bat`)
   - Tomcat will auto-deploy the application

4. **Access the application**
   - http://localhost:8080/backend/giocatori.html

### Option 3: Using Maven Tomcat Plugin

Add this plugin to your `pom.xml` in the `<plugins>` section:

```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <port>8080</port>
        <path>/backend</path>
    </configuration>
</plugin>
```

Then run:
```powershell
mvn tomcat7:run
```

## Important Notes

1. **Context Path:** The `/backend` part in the URL is the context path. Make sure it matches your deployment configuration.

2. **Port 80 vs 8080:** 
   - Port 80 is the default HTTP port (you can omit it in URLs)
   - Port 8080 is Tomcat's default port
   - Using port 80 may require admin privileges

3. **Check web.xml:** Your `web.xml` maps `GiocatoriServlet` to `/secure/giocatori.html`, but the actual HTML file is at `/giocatori.html`

4. **Database Connection:** Make sure your Oracle database is running and accessible with the credentials in your DAO classes.

## Troubleshooting

- **Still getting connection refused?** Make sure Tomcat is actually running (check the console output)
- **404 Not Found?** Check the context path is correct (`/backend`)
- **500 Server Error?** Check Tomcat logs in the `logs` folder or IntelliJ console
- **Database errors?** Verify Oracle connection settings in your DAO classes

