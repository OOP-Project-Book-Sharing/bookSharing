package com.example.first_draft;

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

    public void addItem(CartItem item) {
        items.add(item);
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

