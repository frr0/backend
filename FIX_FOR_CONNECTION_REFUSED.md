# FIX FOR "ERR_CONNECTION_REFUSED" ERROR

## What's Happening

Your professor can access: `http://localhost/backend/giocatori.html`
You get: **ERR_CONNECTION_REFUSED**

## Why This Happens

**ERR_CONNECTION_REFUSED** means:
- No web server is running on your computer
- Your professor has a Tomcat/servlet container running on port 80
- You don't have one running yet!

Think of it like trying to call a phone that's turned off - the connection is refused because nothing is there to answer.

## The Fix (Step by Step)

### Prerequisites Check

1. **Do you have Java installed?**
   - Open Command Prompt and type: `java -version`
   - You should see Java version 11 or higher
   - If not, install JDK from: https://www.oracle.com/java/technologies/downloads/

2. **Set JAVA_HOME (IMPORTANT!)**
   - Find where Java is installed (usually `C:\Program Files\Java\jdk-11.x.x`)
   - Windows Search → "Environment Variables" → "Edit the system environment variables"
   - Click "Environment Variables" button
   - Under "System variables" click "New"
   - Variable name: `JAVA_HOME`
   - Variable value: Your JDK path (e.g., `C:\Program Files\Java\jdk-11.0.12`)
   - Click OK
   - **RESTART your Command Prompt/Terminal after this!**

### Starting Your Application

**Method 1: Using the Startup Script (EASIEST)**

1. Open Command Prompt (cmd) or PowerShell
2. Navigate to your project folder:
   ```
   cd C:\Users\FrancescoRanellucci\Github\Backend
   ```
3. Run:
   ```
   start-server.cmd
   ```
4. Wait for "Running war on http://localhost:8080/backend" message
5. Open browser to: `http://localhost:8080/backend/giocatori.html`

**Note:** Use **8080** not port 80! Your professor might have configured Tomcat differently.

**Method 2: Manual Maven Command**

```
cd C:\Users\FrancescoRanellucci\Github\Backend
mvnw.cmd clean tomcat7:run
```

Then access: `http://localhost:8080/backend/giocatori.html`

**Method 3: Using IntelliJ IDEA (if you're using it)**

1. Open the project in IntelliJ
2. Download Tomcat from https://tomcat.apache.org/download-10.cgi
3. Extract Tomcat somewhere (e.g., C:\apache-tomcat-10.x.x)
4. In IntelliJ: Run → Edit Configurations → Add New → Tomcat Server → Local
5. Configure Tomcat path
6. In Deployment tab: Add artifact → Set context path to `/backend`
7. Click Run

See `DEPLOYMENT_INSTRUCTIONS.md` for detailed IntelliJ setup.

## Common Errors and Solutions

### Error: "JAVA_HOME not found"
**Solution:** Set JAVA_HOME environment variable (see Prerequisites above)

### Error: "Port 8080 already in use"
**Solution:** 
- Something else is using port 8080
- Close other applications (other Tomcat, Xampp, etc.)
- Or change port in pom.xml (line with `<port>8080</port>`)

### Error: "mvn: command not found" or "mvnw.cmd not found"
**Solution:** 
- Use `mvnw.cmd` (Maven wrapper) instead of `mvn`
- It's included in your project
- Or install Maven from https://maven.apache.org/download.cgi

### Error: Database connection issues
**Solution:**
- Make sure Oracle database is running
- Check connection settings in your DAO classes
- Verify username/password/URL are correct

### Still shows "localhost refused to connect"
**Checklist:**
1. ✓ Is the server actually running? (Check terminal for "Running war on...")
2. ✓ Are you using the correct URL? (`http://localhost:8080/backend/giocatori.html`)
3. ✓ Is port 8080 or is it 80? (Most likely 8080 for you)
4. ✓ Check for error messages in the terminal

## Understanding the URL Difference

Your Professor: `http://localhost/backend/giocatori.html`
- No port = port 80 (default HTTP port)
- Requires Tomcat configured to run on port 80
- Requires administrator privileges on Windows

You (Default): `http://localhost:8080/backend/giocatori.html`
- Port 8080 = Tomcat default port
- No special privileges needed
- Easier to set up

Both work the same way, just different ports!

## What I Changed in Your Project

1. **Added Tomcat Maven Plugin to pom.xml**
   - Allows you to run the app without installing Tomcat separately
   - Configured with context path `/backend`
   - Uses port 8080 by default

2. **Created startup scripts**
   - `start-server.cmd` - Easy way to start the server
   - `start-server.ps1` - PowerShell version
   - Automatically uses Maven or Maven wrapper

3. **Created documentation**
   - This file (FIX_FOR_CONNECTION_REFUSED.md)
   - QUICK_START.md - Quick reference
   - DEPLOYMENT_INSTRUCTIONS.md - Detailed deployment guide

## Next Steps

1. Make sure JAVA_HOME is set
2. Run `start-server.cmd`
3. Wait for server to start
4. Access `http://localhost:8080/backend/giocatori.html`
5. If it works, you're done! 🎉

## Still Need Help?

Check these files for more info:
- `QUICK_START.md` - Quick reference
- `DEPLOYMENT_INSTRUCTIONS.md` - Detailed deployment options
- Error messages in terminal - They usually tell you what's wrong

## Summary

**The problem:** No web server running
**The solution:** Run `start-server.cmd`
**The URL to use:** `http://localhost:8080/backend/giocatori.html` (note the 8080!)

