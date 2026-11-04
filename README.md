# Expense Tracker (Java)

A dependency-free, console-based expense management app. Data is stored in a CSV file (`data/expenses.csv`).

## Requirements
- JDK 8 or newer

## Run
```bash
./build.sh      # compile to ./out
./run.sh        # start the app (optionally: ./run.sh path/to/file.csv)
./run-tests.sh  # build + run the self-tests
```
With Maven: `mvn package && java -jar target/expense-tracker-1.0.0.jar`

On Windows (Command Prompt or PowerShell, from the project folder):
```
build.bat       :: compile to .\out
run.bat         :: start the app (optionally: run.bat path\\to\\file.csv)
run-tests.bat   :: build + run the self-tests
```
In PowerShell use `.\run.bat` instead of `run.bat`.

## Features
- Add / delete expenses (date, category, description, amount)
- List all, search by keyword or category
- Monthly report with per-category totals
- Summary by category (with % bars) and by month
- CSV persistence; handles commas and quotes in descriptions

## Structure
```
src/main/java/com/expensetracker/
  Main.java                    entry point
  model/        Expense, Category
  repository/   ExpenseRepository (CSV storage)
  service/      ExpenseService   (business logic, totals)
  ui/           ConsoleApp       (menu / input / output)
src/test/java/...              ExpenseServiceTest (no JUnit needed)
```

## Ideas to extend
Budgets and alerts, edit expense, CSV export/import, a REST API (Spring Boot) or JavaFX UI, a database (SQLite/H2 via JDBC).
