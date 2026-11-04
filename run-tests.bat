@echo off
cd /d "%~dp0"
call build.bat
if errorlevel 1 exit /b 1
if exist out-test rmdir /s /q out-test
mkdir out-test
javac -cp out -d out-test src\test\java\com\expensetracker\ExpenseServiceTest.java
java -cp out;out-test com.expensetracker.ExpenseServiceTest
