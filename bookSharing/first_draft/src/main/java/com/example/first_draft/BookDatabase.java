package com.example.first_draft;

import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class BookDatabase {
    private static final String FILE_PATH = "books.dat";
    private List<Book> books;

    public BookDatabase() {
        books = new ArrayList<>();
        load();
    }

    public void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            books = (List<Book>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error loading book database: " + e.getMessage());
        }
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(books);
        } catch (Exception e) {
            System.out.println("Error saving book database: " + e.getMessage());
        }
    }

    public void addBook(Book book) {
        books.add(book);
        save();
    }

    public void deleteBook(String title) {
        books.removeIf(b -> b.getTitle().equalsIgnoreCase(title));
        save();
    }

    public void updateBook(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equalsIgnoreCase(book.getTitle())) {
                books.set(i, book);
                break;
            }
        }
        save();
    }

    public List<Book> getAllBooks() { return books; }

    public List<Book> getBooksByOwner(String owner) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) if (b.getOwner().equalsIgnoreCase(owner)) result.add(b);
        return result;
    }

    public List<Book> getBooksRentedTo(String user) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) if (user.equalsIgnoreCase(b.getRentedTo())) result.add(b);
        return result;
    }

    public List<Book> getAvailableBooks() {
        List<Book> result = new ArrayList<>();
        for (Book b : books) if (b.isAvailable()) result.add(b);
        return result;
    }
}
