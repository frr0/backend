# 🚀 START HERE - READ THIS FIRST!

## 🎯 The Problem You're Having

Your professor can access the application at `http://localhost/backend/giocatori.html` but when you try, you get:

```
Hmmm… can't reach this page
localhost refused to connect
ERR_CONNECTION_REFUSED
```

## 💡 What This Means

**ERR_CONNECTION_REFUSED = No web server is running on your computer!**

Your professor has a Tomcat server running. You need to start one too!

## ✨ The Quick Fix

### 1️⃣ First Time Setup (Do Once)

**Set JAVA_HOME:**
- Press `Win + R` → type `sysdm.cpl` → Enter
- Advanced tab → Environment Variables
- System variables → New
- Name: `JAVA_HOME`
- Value: Your JDK path (like `C:\Program Files\Java\jdk-11.0.12`)
- OK → **Restart Command Prompt**

### 2️⃣ Start Your Server (Do Every Time)

Open Command Prompt:
```cmd
cd C:\Users\FrancescoRanellucci\Github\Backend
start-server.cmd
```

Wait for: `[INFO] Running war on http://localhost:8080/backend`

### 3️⃣ Open in Browser

Go to: **`http://localhost:8080/backend/giocatori.html`**

⚠️ **IMPORTANT:** Use `8080` (not just `localhost`)!

## 📚 Documentation I Created for You

| File | What's Inside |
|------|---------------|
| **CHECKLIST.md** ⭐ | Step-by-step checklist to get started |
| **FIX_FOR_CONNECTION_REFUSED.md** | Complete fix guide with troubleshooting |
| **QUICK_START.md** | Quick reference guide |
| **DEPLOYMENT_INSTRUCTIONS.md** | Advanced deployment options |
| **start-server.cmd** | Script to start your server (run this!) |
| **start-server.ps1** | PowerShell version of startup script |

## 🔥 What I Changed in Your Project

1. ✅ Modified `pom.xml` - Added Tomcat Maven plugin
2. ✅ Created startup scripts - Easy way to start the server
3. ✅ Created documentation - Multiple guides to help you

## 🎯 Quick Commands

**Start:**
```cmd
start-server.cmd
```

**Stop:**
Press `Ctrl+C` in terminal

**Access:**
```
http://localhost:8080/backend/giocatori.html
```

## ⚠️ Most Common Issues

| Problem | Fix |
|---------|-----|
| "JAVA_HOME not found" | Set JAVA_HOME environment variable (see step 1) |
| "Port 8080 already in use" | Close other apps using port 8080 or restart computer |
| Still connection refused | Server isn't running - check terminal for errors |
| Different port than professor | Professor uses port 80, you use 8080 - both work! |

## 📖 Recommended Reading Order

1. 👉 **START_HERE.md** (You are here!)
2. **CHECKLIST.md** - Follow the checklist
3. **FIX_FOR_CONNECTION_REFUSED.md** - If you have issues
4. **QUICK_START.md** - For quick reference later
5. **DEPLOYMENT_INSTRUCTIONS.md** - For advanced setup

## 🆘 Need Help?

1. Read `CHECKLIST.md` - Follow each step
2. Read `FIX_FOR_CONNECTION_REFUSED.md` - Check troubleshooting section
3. Check terminal errors - They usually tell you what's wrong

## 🎉 Success Looks Like This

When everything works:
1. ✅ Terminal shows: "Running war on http://localhost:8080/backend"
2. ✅ Browser loads: `http://localhost:8080/backend/giocatori.html`
3. ✅ You see the Giocatori page with a table

## 💪 You Got This!

The fix is simple:
1. Set JAVA_HOME (one time)
2. Run `start-server.cmd`
3. Open browser to `http://localhost:8080/backend/giocatori.html`

That's it! No complex setup needed.

---

**Next Step:** Open `CHECKLIST.md` and follow the steps! 🚀

