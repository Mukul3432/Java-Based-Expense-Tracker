package com.expensetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public final class Expense {
    private final int id;
    private final LocalDate date;
    private final Category category;
    private final String description;
    private final BigDecimal amount;

    public Expense(int id, LocalDate date, Category category, String description, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (date == null) throw new IllegalArgumentException("Date is required");
        this.id = id;
        this.date = date;
        this.category = category == null ? Category.OTHER : category;
        this.description = description == null ? "" : description;
        this.amount = amount;
    }

    public int id() { return id; }
    public LocalDate date() { return date; }
    public Category category() { return category; }
    public String description() { return description; }
    public BigDecimal amount() { return amount; }

    /** CSV line: id,date,category,"description",amount */
    public String toCsv() {
        return id + "," + date + "," + category + ",\"" + description.replace("\"", "\"\"") + "\"," + amount.toPlainString();
    }

    public static Expense fromCsv(String line) {
        int p1 = line.indexOf(',');
        int p2 = line.indexOf(',', p1 + 1);
        int p3 = line.indexOf(',', p2 + 1);
        int lastComma = line.lastIndexOf(',');
        int id = Integer.parseInt(line.substring(0, p1));
        LocalDate date = LocalDate.parse(line.substring(p1 + 1, p2));
        Category cat = Category.parse(line.substring(p2 + 1, p3));
        String desc = line.substring(p3 + 1, lastComma);
        if (desc.startsWith("\"") && desc.endsWith("\"") && desc.length() >= 2) {
            desc = desc.substring(1, desc.length() - 1).replace("\"\"", "\"");
        }
        BigDecimal amount = new BigDecimal(line.substring(lastComma + 1));
        return new Expense(id, date, cat, desc, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expense)) return false;
        Expense e = (Expense) o;
        return id == e.id && date.equals(e.date) && category == e.category
                && description.equals(e.description) && amount.compareTo(e.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, category, description);
    }

    @Override
    public String toString() {
        return "Expense{" + toCsv() + "}";
    }
}
