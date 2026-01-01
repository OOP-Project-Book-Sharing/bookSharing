package com.example.first_draft;

import java.io.*;
import java.util.*;

public class BookDatabase {
    private static final String FILE_PATH = "database/books.dat";
    private List<Book> books;

    // Whenever I call this function, it reloads the database from the file.
    public BookDatabase() {
        books = new ArrayList<>();
        try {
            load();
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Can't find this file: " + FILE_PATH);
            save();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("Error reading from file: " + FILE_PATH);
        }
    }

    public void load() throws IOException, ClassNotFoundException{
        File file = new File(FILE_PATH);
        if (!file.exists()){
            throw new FileNotFoundException();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            books = (List<Book>) ois.readObject();
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

    public void deleteBook(String title, String owner) {
        books.removeIf(b -> b.getTitle().equalsIgnoreCase(title) && b.getOwner().equalsIgnoreCase(owner));
        save();
    }

    public void updateBook(Book book) {
        for (int i = 0; i < books.size(); i++) {
            Book existingBook = books.get(i);
            if (existingBook.getTitle().equalsIgnoreCase(book.getTitle()) &&
                    existingBook.getOwner().equalsIgnoreCase(book.getOwner())) {
                books.set(i, book);
                break;
            }
        }
        save();
    }

    public List<Book> getAllBooks() { return books; }

    public List<Book> getAvailableBooksNotOwnedBy(String username) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            boolean notOwnedByUser = !b.getOwner().equalsIgnoreCase(username);
            boolean isAvailable = b.isAvailable();
            boolean notRented = (b.getRentedTo() == null || b.getRentedTo().isEmpty());

            if (notOwnedByUser && isAvailable && notRented) {
                result.add(b);
            }
        }
        return result;
    }
}
