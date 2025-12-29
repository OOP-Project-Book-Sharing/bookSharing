package com.example.first_draft.cart;

import com.example.first_draft.Book;

import java.io.Serializable;
import java.time.LocalDate;

public class CartItem implements Serializable {
    public enum ActionType { BUY, RENT }

    private Book book;
    private ActionType action;
    private int rentalDays; // For rent items
    private LocalDate dueDate; // For rent items

    public CartItem(Book book, ActionType action) {
        this.book = book;
        this.action = action;
        this.rentalDays = 0;
        this.dueDate = null;
    }

    public CartItem(Book book, ActionType action, int rentalDays, LocalDate dueDate) {
        this.book = book;
        this.action = action;
        this.rentalDays = rentalDays;
        this.dueDate = dueDate;
    }

    public Book getBook() { return book; }
    public ActionType getAction() { return action; }
    public int getRentalDays() { return rentalDays; }
    public LocalDate getDueDate() { return dueDate; }

    public void setRentalDays(int days) { this.rentalDays = days; }
    public void setDueDate(LocalDate date) { this.dueDate = date; }
}

