package com.expensetracker;

import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.ui.ConsoleApp;

import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String dataFile = args.length > 0 ? args[0] : "data/expenses.csv";
        ExpenseService service = new ExpenseService(new ExpenseRepository(Paths.get(dataFile)));
        new ConsoleApp(service, new Scanner(System.in)).run();
    }
}
