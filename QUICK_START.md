# QUICK START GUIDE

## The Problem
You're trying to access `http://localhost/backend/giocatori.html` but getting "ERR_CONNECTION_REFUSED"

## The Solution
You need to START a web server with your application deployed!

## Easiest Way to Fix (3 steps):

### Step 1: Open Terminal/Command Prompt in this folder

### Step 2: Run this command:
```
start-server.cmd
```
OR if you prefer PowerShell:
```
.\start-server.ps1
```

### Step 3: Wait for the server to start (you'll see "Running war on http://localhost:8080/backend")

### Step 4: Open your browser and go to:
```
http://localhost:8080/backend/giocatori.html
```

**NOTE:** Use port **8080** not port 80! 
- Your professor might have configured Tomcat to use port 80
- The default is 8080
- If you want to use port 80 like your professor, you need to:
  1. Install and configure Tomcat separately
  2. Run it with administrator privileges
  3. See DEPLOYMENT_INSTRUCTIONS.md for details

## What I Changed

1. Added Tomcat Maven plugin to `pom.xml` - This allows you to run the app easily
2. Set the context path to `/backend` - This matches your professor's URL
3. Created startup scripts - Makes it easy to start the server

## Important URLs

After starting the server, these will work:
- Main page: http://localhost:8080/backend/giocatori.html
- API endpoint: http://localhost:8080/backend/api/giocatori
- Login: http://localhost:8080/backend/login.html

## To Stop the Server

Press `Ctrl+C` in the terminal window where the server is running

## Troubleshooting

**"Maven not found"** → Install Maven from https://maven.apache.org/download.cgi

**"Port 8080 already in use"** → Something else is using port 8080. Close it or change the port in pom.xml

**"Cannot connect to database"** → Make sure Oracle database is running and check connection settings in your DAO classes

**Still having issues?** → Check DEPLOYMENT_INSTRUCTIONS.md for more detailed steps

