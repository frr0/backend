# ✅ GETTING STARTED CHECKLIST

Follow this checklist to get your application running!

## Prerequisites (One-time setup)

### ☐ 1. Verify Java is Installed
Open Command Prompt and run:
```cmd
java -version
```
✅ You should see Java version 11 or higher
❌ If not installed: Download from https://www.oracle.com/java/technologies/downloads/

### ☐ 2. Set JAVA_HOME Environment Variable
1. Press `Win + R`, type `sysdm.cpl`, press Enter
2. Go to "Advanced" tab → Click "Environment Variables"
3. Under "System variables" → Click "New"
4. Variable name: `JAVA_HOME`
5. Variable value: Path to your JDK (e.g., `C:\Program Files\Java\jdk-11.0.12`)
   - To find it: Run `where java` in cmd, then go up two folders
6. Click OK on all windows
7. **IMPORTANT:** Close and reopen any Command Prompt/Terminal windows

### ☐ 3. Verify Maven Wrapper Works
Open Command Prompt in your project folder:
```cmd
cd C:\Users\FrancescoRanellucci\Github\Backend
.\mvnw.cmd --version
```
✅ You should see Maven and Java versions
❌ If error: Check JAVA_HOME is set correctly

## Starting Your Application

### ☐ 4. Run the Startup Script
```cmd
cd C:\Users\FrancescoRanellucci\Github\Backend
start-server.cmd
```

### ☐ 5. Wait for Server to Start
Look for this message in the terminal:
```
[INFO] Running war on http://localhost:8080/backend
```
This means the server is ready!

### ☐ 6. Test in Browser
Open your browser and go to:
```
http://localhost:8080/backend/giocatori.html
```

✅ **Success!** You should see the Giocatori page!

## Troubleshooting

If something doesn't work, check these:

### ☐ JAVA_HOME Error
**Symptom:** "JAVA_HOME not found in your environment"
**Solution:** 
- Set JAVA_HOME (see step 2 above)
- Make sure to restart Command Prompt after setting it

### ☐ Port Already in Use
**Symptom:** "Port 8080 already in use" or "Address already in use"
**Solution:** 
- Check if another application is using port 8080
- Close it or restart your computer
- Common culprits: Other Tomcat, XAMPP, other Java apps

### ☐ Maven/Build Errors
**Symptom:** Compilation errors or Maven errors
**Solution:** 
- Make sure you're in the correct folder
- Try: `.\mvnw.cmd clean install` first
- Check if pom.xml has any syntax errors

### ☐ Connection Still Refused
**Symptom:** Browser still shows "Connection refused"
**Solutions to try:**
1. Make sure the server is actually running (check terminal)
2. Verify you're using the correct URL with port 8080
3. Try `http://127.0.0.1:8080/backend/giocatori.html` instead
4. Check Windows Firewall isn't blocking Java
5. Make sure no VPN is interfering

### ☐ Database Errors
**Symptom:** "Cannot connect to database" or SQL errors
**Solution:** 
- Make sure Oracle database is running
- Check database credentials in your DAO classes
- Verify database URL, username, password are correct

## Quick Reference

**Start server:**
```cmd
start-server.cmd
```

**Stop server:**
Press `Ctrl+C` in the terminal window

**Access application:**
```
http://localhost:8080/backend/giocatori.html
```

**API endpoint:**
```
http://localhost:8080/backend/api/giocatori
```

**View logs:**
Check the terminal window where server is running

## Additional Resources

- 📄 `FIX_FOR_CONNECTION_REFUSED.md` - Complete fix guide
- 📄 `QUICK_START.md` - Quick reference
- 📄 `DEPLOYMENT_INSTRUCTIONS.md` - Advanced deployment options

## Summary

✅ **Before first use:**
1. Install Java
2. Set JAVA_HOME
3. Verify with `.\mvnw.cmd --version`

✅ **Every time you want to run:**
1. Run `start-server.cmd`
2. Wait for "Running war on..." message
3. Open browser to `http://localhost:8080/backend/giocatori.html`

✅ **To stop:**
- Press `Ctrl+C` in the terminal

---

**🎉 Once you complete this checklist, your application will work just like your professor's!**

**💡 Tip:** Bookmark `http://localhost:8080/backend/giocatori.html` in your browser for easy access!

