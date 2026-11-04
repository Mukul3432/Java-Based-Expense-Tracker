package com.expensetracker.service;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExpenseService {
    private final ExpenseRepository repo;

    public ExpenseService(ExpenseRepository repo) {
        this.repo = repo;
    }

    public Expense add(LocalDate date, Category category, String description, BigDecimal amount) {
        return repo.add(new Expense(repo.nextId(), date, category, description, amount));
    }

    public boolean delete(int id) {
        return repo.deleteById(id);
    }

    public List<Expense> listAll() {
        List<Expense> list = new ArrayList<>(repo.findAll());
        list.sort((a, b) -> {
            int c = a.date().compareTo(b.date());
            return c != 0 ? c : Integer.compare(a.id(), b.id());
        });
        return list;
    }

    public List<Expense> listByMonth(YearMonth ym) {
        List<Expense> out = new ArrayList<>();
        for (Expense e : listAll()) {
            if (YearMonth.from(e.date()).equals(ym)) out.add(e);
        }
        return out;
    }

    public List<Expense> search(String keyword) {
        String k = keyword == null ? "" : keyword.toLowerCase();
        List<Expense> out = new ArrayList<>();
        for (Expense e : listAll()) {
            if (e.description().toLowerCase().contains(k)
                    || e.category().name().toLowerCase().contains(k)) {
                out.add(e);
            }
        }
        return out;
    }

    public BigDecimal total(List<Expense> list) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Expense e : list) sum = sum.add(e.amount());
        return sum;
    }

    public Map<Category, BigDecimal> totalsByCategory(List<Expense> list) {
        Map<Category, BigDecimal> map = new TreeMap<>();
        for (Expense e : list) {
            BigDecimal cur = map.get(e.category());
            map.put(e.category(), cur == null ? e.amount() : cur.add(e.amount()));
        }
        return map;
    }

    public Map<YearMonth, BigDecimal> totalsByMonth() {
        Map<YearMonth, BigDecimal> map = new TreeMap<>();
        for (Expense e : listAll()) {
            YearMonth ym = YearMonth.from(e.date());
            BigDecimal cur = map.get(ym);
            map.put(ym, cur == null ? e.amount() : cur.add(e.amount()));
        }
        return map;
    }
}
