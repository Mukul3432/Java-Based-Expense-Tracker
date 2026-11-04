package com.expensetracker.model;

public enum Category {
    FOOD, TRANSPORT, HOUSING, UTILITIES, HEALTH, ENTERTAINMENT, SHOPPING, EDUCATION, OTHER;

    public static Category parse(String s) {
        try {
            return valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            return OTHER;
        }
    }
}
