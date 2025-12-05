================================================================================
                   ROOT CAUSE IDENTIFIED ✓
================================================================================

❌ PROBLEM: Java is NOT installed on your computer
   This is why Maven cannot download dependencies and you see all the errors.

✅ SOLUTION: Install Java, then reload Maven in IntelliJ IDEA

================================================================================

📥 STEP 1: INSTALL JAVA (5 minutes)
================================================================================

1. Go to: https://adoptium.net/

2. Download:
   - Temurin 11 (LTS) - Recommended
   - OR Temurin 17 (LTS)
   - OR Temurin 21 (LTS)

   Choose the MSI installer for Windows

3. Run the installer
   ✓ Accept all defaults
   ✓ Make sure "Set JAVA_HOME variable" is checked
   ✓ Make sure "Add to PATH" is checked

4. Restart your computer (or at least restart IntelliJ IDEA)

================================================================================

⚙️ STEP 2: CONFIGURE INTELLIJ IDEA (1 minute)
================================================================================

After installing Java and restarting:

1. Open your project in IntelliJ IDEA

2. File > Project Structure (or press Ctrl+Alt+Shift+S)

3. Under "Project Settings" > "Project":
   - SDK: Select the Java 11 you just installed
     (If not listed, click "Add SDK" > "Download JDK")
   - Language level: "11 - Local variable syntax for lambda parameters"

4. Click "Apply" then "OK"

================================================================================

🔄 STEP 3: RELOAD MAVEN (2 minutes)
================================================================================

1. Open Maven tool window:
   - View > Tool Windows > Maven
   - OR click "Maven" tab on right side of window

2. Click the "Reload All Maven Projects" button 🔄
   - It's in the top-left of the Maven window
   - Look for circular arrows icon

3. Wait for download to complete
   - Progress bar at bottom of IntelliJ
   - Should see "BUILD SUCCESS" when done
   - First time takes 2-5 minutes (downloading ~20-30 MB)

================================================================================

✅ STEP 4: VERIFY (30 seconds)
================================================================================

Check these things:

1. pom.xml - no more red underlines or errors

2. GiocatoriRestServlet.java:
   - "import com.google.gson.Gson;" - should work
   - No "Cannot resolve symbol 'google'" error

3. Project builds successfully:
   - Build > Build Project (Ctrl+F9)
   - Should complete without errors

================================================================================

📚 WHAT YOU FIXED
================================================================================

✅ Installed Java 11+ on your system
✅ Configured IntelliJ IDEA to use Java
✅ Maven can now run and download dependencies
✅ Downloaded dependencies:
   - gson-2.8.9.jar (JSON library)
   - jackson-databind-2.13.4.jar (JSON processing)
   - ojdbc11-21.5.0.0.jar (Oracle JDBC driver)
   - jakarta.servlet-api-6.0.0.jar (Servlet API)
   - jakarta.servlet.jsp.jstl-2.0.0.jar (JSP tags)
   - All transitive dependencies

✅ Your Java source files from src/it/ are now included in build
✅ Project compiles successfully

================================================================================

🆘 STILL HAVING ISSUES?
================================================================================

If you still see errors after completing all steps:

1. Check Event Log (bottom-right corner of IntelliJ)
   - Look for specific error messages

2. Try "Invalidate Caches and Restart":
   - File > Invalidate Caches...
   - Select "Invalidate and Restart"

3. Check Internet connection:
   - Maven downloads from: https://repo.maven.apache.org/maven2
   - Check if firewall/proxy is blocking

4. Verify Java installation:
   - Open PowerShell
   - Run: java -version
   - Should show Java 11 or higher

================================================================================

Need more help? Check these files:
- FIX_INSTRUCTIONS.md (detailed instructions)
- QUICK_FIX_GUIDE.txt (visual guide)

================================================================================

