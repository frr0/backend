# How to Fix Maven Dependency Errors - UPDATED

## What Was Fixed
✅ Updated `pom.xml` with:
- Maven Central repository configuration
- Downgraded Gson to 2.8.9 (more stable)
- Downgraded Jackson to 2.13.4 (more stable)
- Added build-helper-maven-plugin for `src/it` directory
- Added explicit Maven compiler plugin configuration

## Problem
The errors are showing because IntelliJ IDEA needs to download the Maven dependencies, but it requires:
1. **Java to be properly configured**
2. **Maven to be reloaded/reimported in the IDE**

## QUICK FIX - Try This First! 🚀

### Step 1: Configure Java in IntelliJ
1. Open **File > Project Structure** (or press `Ctrl+Alt+Shift+S`)
2. Under **Project Settings > Project**:
   - Set **SDK** to Java 11 or higher (if you don't have one, click "Download JDK")
   - Set **Language level** to "11 - Local variable syntax for lambda parameters"
3. Click **Apply** and **OK**

### Step 2: Reload Maven Project
1. Look for the **Maven** tool window on the right side of IntelliJ
   - If you don't see it: **View > Tool Windows > Maven**
2. In the Maven tool window, click the **🔄 Reload All Maven Projects** button
   - It's the circular arrows icon at the top left of the Maven window
3. Wait for the download to complete (watch the progress bar at the bottom)

### Step 3: Verify
After Maven finishes (might take 1-5 minutes):
- ✅ `pom.xml` should have no red underlines
- ✅ `GiocatoriRestServlet.java` should not show "Cannot resolve symbol 'google'"
- ✅ All Java files should compile without import errors

---

## Alternative Fix (If Quick Fix Doesn't Work)

### Option A: Set Maven JRE
1. **File > Settings** (or `Ctrl+Alt+S`)
2. Go to **Build, Execution, Deployment > Build Tools > Maven > Runner**
3. Set **JRE** to Java 11 or higher (not "Use Project JDK")
4. Click **OK**
5. Reload Maven project (see Step 2 above)

### Option B: Use IntelliJ's Bundled Maven
1. **File > Settings** (`Ctrl+Alt+S`)
2. Go to **Build, Execution, Deployment > Build Tools > Maven**
3. Set **Maven home path** to "Bundled (Maven 3.x)"
4. Click **OK**
5. Reload Maven project (see Step 2 above)

### Option C: Run PowerShell Script
I've created a script that will:
- Find Java on your system
- Set JAVA_HOME
- Run Maven to download dependencies

**To run it:**
```powershell
cd C:\Users\FrancescoRanellucci\Github\Backend
powershell -ExecutionPolicy Bypass -File .\reload-maven.ps1
```

---

## If Java Is Not Installed

Download and install Java 11 or later:
- **Recommended**: [Eclipse Temurin (AdoptiumJDK)](https://adoptium.net/)
- **Alternative**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)

After installing Java, restart IntelliJ IDEA and follow the Quick Fix steps above.

---

## What Will Be Downloaded

Once Maven runs successfully, it will download:
- ✅ **gson-2.8.9.jar** - JSON serialization library
- ✅ **jackson-databind-2.13.4.jar** (+ core, annotations) - JSON processing
- ✅ **ojdbc11-21.5.0.0.jar** - Oracle database driver
- ✅ **jakarta.servlet-api-6.0.0.jar** - Servlet API
- ✅ **jakarta.servlet.jsp.jstl-2.0.0.jar** - JSP Standard Tag Library
- ✅ **All transitive dependencies**

Total download size: ~20-30 MB

---

## Still Having Issues?

If none of the above works:
1. Check IntelliJ IDEA's **Event Log** (bottom right corner) for specific errors
2. Try **File > Invalidate Caches > Invalidate and Restart**
3. Check that you have internet connection (Maven needs to download from repo.maven.apache.org)
4. Check if a proxy or firewall is blocking Maven downloads

