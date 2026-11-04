package com.expensetracker.ui;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {
    private final ExpenseService service;
    private final Scanner in;

    public ConsoleApp(ExpenseService service, Scanner in) {
        this.service = service;
        this.in = in;
    }

    public void run() {
        System.out.println("=== Expense Tracker ===");
        while (true) {
            printMenu();
            String choice = prompt("Choose an option");
            if (choice == null) return;
            try {
                switch (choice.trim()) {
                    case "1": addExpense(); break;
                    case "2": printTable(service.listAll()); break;
                    case "3": monthlyReport(); break;
                    case "4": categorySummary(); break;
                    case "5": search(); break;
                    case "6": delete(); break;
                    case "0":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1) Add expense");
        System.out.println("2) List all expenses");
        System.out.println("3) Monthly report");
        System.out.println("4) Summary by category");
        System.out.println("5) Search");
        System.out.println("6) Delete expense");
        System.out.println("0) Exit");
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    private String prompt(String label) {
        System.out.print(label + ": ");
        return in.hasNextLine() ? in.nextLine() : null;
    }

    private void addExpense() {
        String d = prompt("Date (yyyy-MM-dd, blank = today)");
        LocalDate date = isBlank(d) ? LocalDate.now() : LocalDate.parse(d.trim());
        System.out.println("Categories: " + Arrays.toString(Category.values()));
        String c = prompt("Category");
        Category cat = c == null ? Category.OTHER : Category.parse(c);
        String desc = prompt("Description");
        BigDecimal amount = new BigDecimal(prompt("Amount").trim()).setScale(2, RoundingMode.HALF_UP);
        Expense e = service.add(date, cat, desc, amount);
        System.out.println("Added expense #" + e.id());
    }

    private void monthlyReport() {
        String m = prompt("Month (yyyy-MM, blank = current)");
        YearMonth ym = isBlank(m) ? YearMonth.now() : YearMonth.parse(m.trim());
        List<Expense> list = service.listByMonth(ym);
        System.out.println("--- Report for " + ym + " ---");
        printTable(list);
        for (Map.Entry<Category, BigDecimal> en : service.totalsByCategory(list).entrySet()) {
            System.out.printf("  %-14s %10s%n", en.getKey(), en.getValue());
        }
    }

    private void categorySummary() {
        List<Expense> all = service.listAll();
        BigDecimal total = service.total(all);
        System.out.println("--- Spending by category ---");
        for (Map.Entry<Category, BigDecimal> en : service.totalsByCategory(all).entrySet()) {
            double pct = total.signum() == 0 ? 0 : en.getValue().doubleValue() * 100 / total.doubleValue();
            System.out.printf("  %-14s %10s  %5.1f%%  %s%n",
                    en.getKey(), en.getValue(), pct, repeat("#", (int) (pct / 4)));
        }
        System.out.println("--- Spending by month ---");
        for (Map.Entry<YearMonth, BigDecimal> en : service.totalsByMonth().entrySet()) {
            System.out.printf("  %-14s %10s%n", en.getKey(), en.getValue());
        }
        System.out.println("Grand total: " + total);
    }

    private void search() {
        printTable(service.search(prompt("Keyword")));
    }

    private void delete() {
        int id = Integer.parseInt(prompt("Expense id to delete").trim());
        System.out.println(service.delete(id) ? "Deleted." : "No expense with that id.");
    }

    private void printTable(List<Expense> list) {
        if (list.isEmpty()) {
            System.out.println("(no expenses)");
            return;
        }
        String line = repeat("-", 71);
        System.out.printf("%-4s %-11s %-14s %-28s %10s%n", "ID", "Date", "Category", "Description", "Amount");
        System.out.println(line);
        for (Expense e : list) {
            String desc = e.description().length() > 28 ? e.description().substring(0, 25) + "..." : e.description();
            System.out.printf("%-4d %-11s %-14s %-28s %10s%n", e.id(), e.date(), e.category(), desc, e.amount());
        }
        System.out.println(line);
        System.out.printf("%-60s %10s%n", "Total", service.total(list));
    }
}
