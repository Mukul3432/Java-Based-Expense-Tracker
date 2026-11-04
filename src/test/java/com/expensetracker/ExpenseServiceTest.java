package com.expensetracker;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.service.ExpenseService;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

/** Dependency-free self-test (run with run-tests.sh). */
public class ExpenseServiceTest {
    static void check(boolean cond, String msg) {
        if (!cond) throw new AssertionError("FAILED: " + msg);
        System.out.println("PASS: " + msg);
    }

    public static void main(String[] args) throws Exception {
        Path tmp = Files.createTempFile("expenses", ".csv");
        Files.delete(tmp);
        ExpenseService s = new ExpenseService(new ExpenseRepository(tmp));

        s.add(LocalDate.of(2026, 10, 1), Category.FOOD, "Lunch, with \"friends\"", new BigDecimal("12.50"));
        s.add(LocalDate.of(2026, 10, 2), Category.TRANSPORT, "Metro", new BigDecimal("3.00"));
        s.add(LocalDate.of(2026, 9, 15), Category.FOOD, "Groceries", new BigDecimal("40.00"));

        check(s.listAll().size() == 3, "three expenses stored");
        check(s.total(s.listAll()).compareTo(new BigDecimal("55.50")) == 0, "grand total is 55.50");
        check(s.totalsByCategory(s.listAll()).get(Category.FOOD).compareTo(new BigDecimal("52.50")) == 0, "food total");

        ExpenseService reloaded = new ExpenseService(new ExpenseRepository(tmp));
        Expense first = reloaded.listAll().stream().filter(e -> e.id() == 1).findFirst().orElseThrow(() -> new AssertionError("expense 1 missing"));
        check(first.description().equals("Lunch, with \"friends\""), "CSV round-trip keeps commas and quotes");

        check(reloaded.delete(2) && reloaded.listAll().size() == 2, "delete works and persists");
        check(reloaded.search("grocer").size() == 1, "search by keyword");

        try {
            s.add(LocalDate.now(), Category.FOOD, "bad", BigDecimal.ZERO);
            check(false, "zero amount rejected");
        } catch (IllegalArgumentException ex) {
            check(true, "zero amount rejected");
        }
        Files.deleteIfExists(tmp);
        System.out.println("All tests passed.");
    }
}
