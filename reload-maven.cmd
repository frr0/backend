@echo off
echo Reloading Maven dependencies...
call mvnw.cmd clean compile -U
pause

