package com.example.first_draft.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {
    private static Cart instance;
    private final List<CartItem> items = new ArrayList<>();

    private Cart() {}

    public static Cart getInstance() {
        if (instance == null) instance = new Cart();
        return instance;
    }

    public boolean addItem(CartItem item) {
        if (containsBook(item.getBook())) {
            return false;
        }
        items.add(item);
        return true;
    }

    public boolean containsBook(com.example.first_draft.Book book) {
        for (CartItem item : items) {
            if (isSameBook(item.getBook(), book)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSameBook(com.example.first_draft.Book book1, com.example.first_draft.Book book2) {
        return book1.getTitle().equals(book2.getTitle()) &&
                book1.getOwner().equals(book2.getOwner());
    }

    public void removeItem(CartItem item) {
        items.remove(item);
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void clear() {
        items.clear();
    }
}

