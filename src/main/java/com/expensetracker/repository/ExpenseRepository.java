package com.expensetracker.repository;

import com.expensetracker.model.Expense;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Simple CSV-file persistence. No external dependencies. */
public class ExpenseRepository {
    private static final String HEADER = "id,date,category,description,amount";
    private final Path file;
    private final List<Expense> expenses = new ArrayList<>();

    public ExpenseRepository(Path file) {
        this.file = file;
        load();
    }

    private void load() {
        try {
            Path parent = file.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            if (!Files.exists(file)) {
                Files.write(file, Collections.singletonList(HEADER));
                return;
            }
            for (String line : Files.readAllLines(file)) {
                if (line.trim().isEmpty() || line.equals(HEADER)) continue;
                expenses.add(Expense.fromCsv(line));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + file, e);
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Expense e : expenses) lines.add(e.toCsv());
        try {
            Files.write(file, lines);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write " + file, e);
        }
    }

    public List<Expense> findAll() {
        return Collections.unmodifiableList(expenses);
    }

    public Expense add(Expense e) {
        expenses.add(e);
        save();
        return e;
    }

    public boolean deleteById(int id) {
        boolean removed = false;
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).id() == id) {
                expenses.remove(i);
                removed = true;
                break;
            }
        }
        if (removed) save();
        return removed;
    }

    public int nextId() {
        int max = 0;
        for (Expense e : expenses) max = Math.max(max, e.id());
        return max + 1;
    }
}
