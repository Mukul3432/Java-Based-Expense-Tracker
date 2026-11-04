@echo off
cd /d "%~dp0"
if exist out rmdir /s /q out
mkdir out
javac -d out src\main\java\com\expensetracker\*.java src\main\java\com\expensetracker\model\*.java src\main\java\com\expensetracker\repository\*.java src\main\java\com\expensetracker\service\*.java src\main\java\com\expensetracker\ui\*.java
if errorlevel 1 (
  echo.
  echo Build FAILED. Make sure a JDK 8+ is installed: run "javac -version"
  exit /b 1
)
echo Build OK
